package com.desafio.votacao.service;

import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.Data.Entity.Proposal;
import com.desafio.votacao.Data.Enums.StatusEnum;
import com.desafio.votacao.Data.Repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepository;

    private Proposal proposal;
    private ProposalRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = ProposalRequestDto.builder()
                .title("Nova Proposta")
                .description("Descrição teste")
                .creator("João")
                .votingStart(LocalDateTime.now())
                .votingEnd(LocalDateTime.now().plusMinutes(5))
                .build();

        proposal = Proposal.builder()
                .id(1L)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .creator(requestDto.getCreator())
                .createdAt(LocalDateTime.now())
                .startVotation(requestDto.getVotingStart())
                .endVotation(requestDto.getVotingEnd())
                .status(StatusEnum.OPEN)
                .yesVotes(0)
                .noVotes(0)
                .build();
    }

    @Test
    void shouldCreateProposal() {
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal);

        ProposalResponseDto response = proposalService.createProposal(requestDto);

        assertNotNull(response);
        assertEquals("Nova Proposta", response.getTitle());
        verify(proposalRepository, times(1)).save(any(Proposal.class));
    }

    @Test
    void shouldCloseVotingAndApproveProposal() {
        proposal.setYesVotes(10);
        proposal.setNoVotes(5);
        proposal.setEndVotation(LocalDateTime.now().plusMinutes(2));

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal);

        ProposalResponseDto response = proposalService.closeVoting(1L);

        assertEquals("APPROVED", response.getResult());
        verify(proposalRepository).save(proposal);
    }

    @Test
    void shouldCloseVotingAndRejectProposal() {
        proposal.setYesVotes(2);
        proposal.setNoVotes(5);

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal);

        ProposalResponseDto response = proposalService.closeVoting(1L);

        assertEquals("REJECTED", response.getResult());
    }

    @Test
    void shouldThrowExceptionWhenProposalNotFound() {
        when(proposalRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proposalService.closeVoting(999L);
        });

        assertEquals("Proposal not found", exception.getMessage());
    }

}
