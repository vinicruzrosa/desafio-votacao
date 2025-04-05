package com.desafio.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(String cpf) {
        super("CPF não encontrado ou inválido: " + cpf);
    }
}