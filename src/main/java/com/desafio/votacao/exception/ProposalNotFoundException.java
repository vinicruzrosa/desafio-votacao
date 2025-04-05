package com.desafio.votacao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProposalNotFoundException extends RuntimeException {
    public ProposalNotFoundException(Long id) {
        super("Proposta com ID " + id + " não encontrada.");
    }
}