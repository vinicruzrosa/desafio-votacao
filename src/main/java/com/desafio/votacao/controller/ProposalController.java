package com.desafio.votacao.controller;

import com.desafio.votacao.Data.Dtos.ProposalRequestDto;
import com.desafio.votacao.Data.Dtos.ProposalResponseDto;
import com.desafio.votacao.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
