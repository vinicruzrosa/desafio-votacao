package com.desafio.votacao.Data.Dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class ProposalRequestDto {
    private String title;
    private String description;
    private String creator;
    private LocalDateTime votingStart;
    private LocalDateTime votingEnd;

    public ProposalRequestDto(String title, String description, String creator,
                              LocalDateTime votingStart, LocalDateTime votingEnd) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.votingStart = votingStart;
        this.votingEnd = votingEnd;
    }
}
