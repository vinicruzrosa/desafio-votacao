package com.desafio.votacao.Data.Dtos;

import com.desafio.votacao.Data.Enums.StatusEnum;
import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProposalResponseDto {
    private Long id;
    private String title;
    private String description;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime startVotation;
    private LocalDateTime closeVotation;
    private StatusEnum status;
    private Integer yesVotes;
    private Integer noVotes;
    private String result;

}
