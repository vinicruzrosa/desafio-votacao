package com.desafio.votacao.service;


import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.Data.Dtos.VoteRequestDto;
import com.desafio.votacao.Data.Entity.Proposal;
import com.desafio.votacao.Data.Entity.Vote;
import com.desafio.votacao.Data.Enums.StatusEnum;
import com.desafio.votacao.Data.Enums.VoteValue;
import com.desafio.votacao.Data.Repository.ProposalRepository;
import com.desafio.votacao.Data.Repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProposalService {
    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private VoteRepository voteRepository;

    private static final Logger log = LoggerFactory.getLogger(ProposalService.class);

    public ProposalResponseDto createProposal(ProposalRequestDto proposalRequestDto) {
        Proposal proposal = Proposal.builder()
                .title(proposalRequestDto.getTitle())
                .description(proposalRequestDto.getDescription())
                .creator(proposalRequestDto.getCreator())
                .status(StatusEnum.PENDING)
                .createdAt(LocalDateTime.now())
                .startVotation(proposalRequestDto.getVotingStart())
                .endVotation(proposalRequestDto.getVotingEnd())
                .yesVotes(0)
                .noVotes(0)
                .result("Pending")
                .build();

        log.debug("Proposal Builded Successfully");
        proposal = proposalRepository.save(proposal);
        log.info("Proposal Saved Successfully");
        return toResponseDto(proposal);
    }

    public void openVotingSession(Long id, Integer durationMinutes) {
        Proposal proposal = proposalRepository.findById(id).orElseThrow();
        proposal.setStatus(StatusEnum.OPEN);
        proposal.setStartVotation(LocalDateTime.now());
        proposal.setEndVotation(LocalDateTime.now().plusMinutes(durationMinutes == null ? 1 : durationMinutes));
        proposalRepository.save(proposal);
        log.info("Voting session opened for proposal {}", id);
    }

    public void castVote(Long proposalId, VoteRequestDto dto) {
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow();

        if (!proposal.getStatus().equals(StatusEnum.OPEN) ||
                LocalDateTime.now().isAfter(proposal.getEndVotation())) {
            throw new IllegalStateException("Voting session is not open or has expired.");
        }

        voteRepository.findByAssociateIdAndProposalId(dto.getAssociateId(), proposalId)
                .ifPresent(v -> { throw new IllegalArgumentException("Associate already voted."); });

        Vote vote = Vote.builder()
                .associateId(dto.getAssociateId())
                .voteValue(dto.getValue())
                .proposal(proposal)
                .build();

        voteRepository.save(vote);

        if (dto.getValue() == VoteValue.YES) proposal.setYesVotes(proposal.getYesVotes() + 1);
        else proposal.setNoVotes(proposal.getNoVotes() + 1);

        proposalRepository.save(proposal);
        log.info("Vote registered for proposal {} by associate {}", proposalId, dto.getAssociateId());
    }

    public ProposalResponseDto closeVotingAndGetResult(Long id) {
        Proposal proposal = proposalRepository.findById(id).orElseThrow();

        if (LocalDateTime.now().isBefore(proposal.getEndVotation())) {
            throw new IllegalStateException("Voting still in progress");
        }

        proposal.setStatus(StatusEnum.CLOSED);
        String result = proposal.getYesVotes() > proposal.getNoVotes() ? "APPROVED" : "REJECTED";
        proposal.setResult(result);

        proposalRepository.save(proposal);
        log.info("Voting session closed for proposal {}. Result: {}", id, result);
        return toResponseDto(proposal);
    }


    public ProposalResponseDto closeVoting(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found"));

        if (proposal.getEndVotation().isAfter(LocalDateTime.now())) {
            proposal.setEndVotation(LocalDateTime.now()); // Encerra a votação imediatamente
        }

        // Contabiliza votos (exemplo simplificado)
        String result = proposal.getYesVotes() >= proposal.getNoVotes() ? "APPROVED" : "REJECTED";
        proposal.setResult(result);

        proposalRepository.save(proposal);
        return toResponseDto(proposal);
    }


    private ProposalResponseDto toResponseDto(Proposal proposal) {
        return ProposalResponseDto.builder()
                .id(proposal.getId())
                .title(proposal.getTitle())
                .description(proposal.getDescription())
                .creator(proposal.getCreator())
                .status(StatusEnum.valueOf(proposal.getStatus().name()))
                .startVotation(proposal.getStartVotation())
                .closeVotation(proposal.getEndVotation())
                .yesVotes(proposal.getYesVotes())
                .noVotes(proposal.getNoVotes())
                .result(proposal.getResult())
                .build();
    }



}
