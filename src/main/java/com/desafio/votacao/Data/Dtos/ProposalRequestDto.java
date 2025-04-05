package com.desafio.votacao.Data.Dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProposalRequestDto {
    private String title;
    private String description;
    private String creator;
    private LocalDateTime votingStart;
    private LocalDateTime votingEnd;
}
