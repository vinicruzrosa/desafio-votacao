package com.desafio.votacao.Data.Dtos;

import com.desafio.votacao.Data.Enums.VoteValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteRequestDto {
    private String associateId;
    private VoteValue value;

    public VoteRequestDto(String associateId, VoteValue value) {
        this.associateId = associateId;
        this.value = value;
    }
}