package com.desafio.votacao.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateVoteException extends RuntimeException {
    public DuplicateVoteException(String cpf, Long proposalId) {
        super("Usuário com CPF " + cpf + " já votou na proposta " + proposalId);
    }
}