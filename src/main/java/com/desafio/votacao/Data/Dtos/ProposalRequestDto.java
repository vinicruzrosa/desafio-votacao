package com.desafio.votacao.Data.Dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProposalRequestDto {
    private String title;
    private String description;
    private String creator;
    private LocalDateTime votingStart;
    private LocalDateTime votingEnd;
}
