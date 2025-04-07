package com.desafio.votacao.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ProposalDeclinedException extends RuntimeException {

    public ProposalDeclinedException(String message) {
        super(message);
    }

    public ProposalDeclinedException() {
        super("Proposta recusada pela análise externa.");
    }
}
