package com.desafio.votacao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserUnableToVoteException extends RuntimeException {
    public UserUnableToVoteException(String cpf) {
        super("Usuário com CPF " + cpf + " não pode votar.");
    }
}