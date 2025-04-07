package com.desafio.votacao.controller;

import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.Data.Dtos.VoteRequestDto;
import com.desafio.votacao.Data.Enums.StatusEnum;
import com.desafio.votacao.Data.Enums.VoteValue;
import com.desafio.votacao.service.ProposalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalControllerTest {

    @InjectMocks
    private ProposalController proposalController;

    @Mock
    private ProposalService proposalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostProposal() {
        ProposalRequestDto requestDto = new ProposalRequestDto("Title", "Desc", "John", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));
        ProposalResponseDto responseDto = ProposalResponseDto.builder()
                .id(1L)
                .title("Title")
                .description("Desc")
                .creator("John")
                .status(StatusEnum.PENDING)
                .startVotation(LocalDateTime.now())
                .closeVotation(LocalDateTime.now().plusMinutes(10))
                .yesVotes(0)
                .noVotes(0)
                .result("Pending")
                .build();

        when(proposalService.createProposal(requestDto)).thenReturn(responseDto);

        ResponseEntity<ProposalResponseDto> response = proposalController.createProposal(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Title", Objects.requireNonNull(response.getBody()).getTitle());
    }

    @Test
    void testOpenSession() {
        Long proposalId = 1L;
        Integer minutes = 5;

        ResponseEntity<String> response = proposalController.openSession(proposalId, minutes);

        verify(proposalService).openVotingSession(proposalId, minutes);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Voting session opened", response.getBody());
    }

    @Test
    void testVote() {
        Long proposalId = 1L;
        VoteRequestDto voteRequestDto = new VoteRequestDto("123456789", VoteValue.YES);

        ResponseEntity<String> response = proposalController.vote(proposalId, voteRequestDto);

        verify(proposalService).castVote(proposalId, voteRequestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vote registered", response.getBody());
    }

    @Test
    void testGetResult() {
        Long proposalId = 1L;
        ProposalResponseDto responseDto = ProposalResponseDto.builder()
                .id(proposalId)
                .title("Title")
                .result("APPROVED")
                .status(StatusEnum.CLOSED)
                .yesVotes(5)
                .noVotes(2)
                .build();

        when(proposalService.closeVotingAndGetResult(proposalId)).thenReturn(responseDto);

        ResponseEntity<ProposalResponseDto> response = proposalController.getResult(proposalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("APPROVED", Objects.requireNonNull(response.getBody()).getResult());
    }
}
