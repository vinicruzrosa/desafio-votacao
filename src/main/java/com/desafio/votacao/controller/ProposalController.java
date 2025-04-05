package com.desafio.votacao.controller;

import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.Data.Dtos.VoteRequestDto;
import com.desafio.votacao.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping
    private ResponseEntity<ProposalResponseDto> post(@RequestBody ProposalRequestDto requestDto) {
        ProposalResponseDto proposalResponse = proposalService.createProposal(requestDto);
        return new ResponseEntity<>(proposalResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<String> openSession(@PathVariable Long id, @RequestParam(required = false) Integer minutes) {
        proposalService.openVotingSession(id, minutes);
        return ResponseEntity.ok("Voting session opened");
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<String> vote(@PathVariable Long id, @RequestBody VoteRequestDto voteRequestDto) {
        proposalService.castVote(id, voteRequestDto);
        return ResponseEntity.ok("Vote registered");
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<ProposalResponseDto> getResult(@PathVariable Long id) {
        return ResponseEntity.ok(proposalService.closeVotingAndGetResult(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<ProposalResponseDto> closeVoting(@PathVariable Long id) {
        ProposalResponseDto closedProposal = proposalService.closeVoting(id);
        return ResponseEntity.ok(closedProposal);
    }

}
