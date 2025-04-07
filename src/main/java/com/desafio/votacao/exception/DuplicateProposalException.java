package com.desafio.votacao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateProposalException extends RuntimeException {
    public DuplicateProposalException(String title) {
        super("Já existe uma proposta encerrada com o título: " + title);
    }
}
