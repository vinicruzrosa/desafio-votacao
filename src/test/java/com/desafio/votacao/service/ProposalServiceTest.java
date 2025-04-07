package com.desafio.votacao.service;


import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.Data.Dtos.ProposalStatusResponse;
import com.desafio.votacao.Data.Dtos.VoteRequestDto;
import com.desafio.votacao.Data.Entity.Proposal;
import com.desafio.votacao.Data.Entity.Vote;
import com.desafio.votacao.Data.Enums.StatusEnum;
import com.desafio.votacao.Data.Enums.VoteValue;
import com.desafio.votacao.Data.Repository.ProposalRepository;
import com.desafio.votacao.Data.Repository.VoteRepository;
import com.desafio.votacao.client.ProposalValidatorClient;
import com.desafio.votacao.exception.ProposalDeclinedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ProposalValidatorClient validatorClient;

    @InjectMocks
    private ProposalService proposalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProposalSuccessfully() {
        ProposalRequestDto request = ProposalRequestDto.builder()
                .title("Nova Proposta")
                .description("Descrição")
                .creator("Fulano")
                .votingStart(LocalDateTime.now())
                .votingEnd(LocalDateTime.now().plusMinutes(10))
                .build();

        when(validatorClient.validateProposal()).thenReturn(new ProposalStatusResponse("APPROVED"));
        when(proposalRepository.findByTitleIgnoreCaseAndStatus(any(), eq(StatusEnum.CLOSED))).thenReturn(java.util.Collections.emptyList());

        Proposal proposalSaved = Proposal.builder()
                .id(1L)
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(request.getCreator())
                .status(StatusEnum.PENDING)
                .startVotation(request.getVotingStart())
                .endVotation(request.getVotingEnd())
                .yesVotes(0)
                .noVotes(0)
                .result("Pending")
                .createdAt(LocalDateTime.now())
                .build();

        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposalSaved);

        ProposalResponseDto response = proposalService.createProposal(request);

        assertNotNull(response);
        assertEquals("Nova Proposta", response.getTitle());
        verify(proposalRepository).save(any(Proposal.class));
    }

    @Test
    void shouldThrowProposalDeclinedException() {
        ProposalRequestDto request = ProposalRequestDto.builder()
                .title("Proposta")
                .description("Desc")
                .creator("User")
                .votingStart(LocalDateTime.now())
                .votingEnd(LocalDateTime.now().plusMinutes(10))
                .build();

        when(validatorClient.validateProposal()).thenReturn(new ProposalStatusResponse("DECLINED"));

        assertThrows(ProposalDeclinedException.class, () -> proposalService.createProposal(request));
    }

    @Test
    void shouldOpenVotingSession() {
        Proposal proposal = Proposal.builder()
                .id(1L)
                .title("Teste")
                .status(StatusEnum.PENDING)
                .build();

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

        proposalService.openVotingSession(1L, 5);

        assertEquals(StatusEnum.OPEN, proposal.getStatus());
        assertNotNull(proposal.getStartVotation());
        assertNotNull(proposal.getEndVotation());
        verify(proposalRepository).save(proposal);
    }

    @Test
    void shouldCastVoteSuccessfully() {
        Proposal proposal = Proposal.builder()
                .id(1L)
                .title("Teste")
                .yesVotes(0)
                .noVotes(0)
                .build();

        VoteRequestDto dto = VoteRequestDto.builder()
                .associateId("123")
                .value(VoteValue.YES)
                .build();

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(voteRepository.findByAssociateIdAndProposalId("123", 1L)).thenReturn(Optional.empty());

        proposalService.castVote(1L, dto);

        assertEquals(1, proposal.getYesVotes());
        verify(voteRepository).save(any(Vote.class));
        verify(proposalRepository).save(proposal);
    }

    @Test
    void shouldNotAllowDuplicateVote() {
        Proposal proposal = Proposal.builder().id(1L).build();

        VoteRequestDto dto = VoteRequestDto.builder()
                .associateId("123")
                .value(VoteValue.NO)
                .build();

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(voteRepository.findByAssociateIdAndProposalId("123", 1L))
                .thenReturn(Optional.of(mock(Vote.class)));

        assertThrows(IllegalArgumentException.class, () -> proposalService.castVote(1L, dto));
    }

    @Test
    void shouldCloseVotingAndReturnResult() {
        Proposal proposal = Proposal.builder()
                .id(1L)
                .yesVotes(10)
                .noVotes(5)
                .endVotation(LocalDateTime.now().minusMinutes(1))
                .status(StatusEnum.OPEN)
                .build();

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

        ProposalResponseDto response = proposalService.closeVotingAndGetResult(1L);

        assertEquals("APPROVED", response.getResult());
        assertEquals(StatusEnum.CLOSED, response.getStatus());
        verify(proposalRepository).save(proposal);
    }

    @Test
    void shouldThrowWhenClosingBeforeEndTime() {
        Proposal proposal = Proposal.builder()
                .id(1L)
                .endVotation(LocalDateTime.now().plusMinutes(5))
                .build();

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

        assertThrows(IllegalStateException.class, () -> proposalService.closeVotingAndGetResult(1L));
    }

}
