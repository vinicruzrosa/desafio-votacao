package com.desafio.votacao.Data.Dtos;


import lombok.Getter;

@Getter
public class ProposalStatusResponse {
    private String status;

    public ProposalStatusResponse(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
