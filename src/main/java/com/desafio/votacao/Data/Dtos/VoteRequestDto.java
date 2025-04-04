package com.desafio.votacao.Data.Dtos;

import com.desafio.votacao.Data.Enums.VoteValue;
import lombok.Data;

@Data
public class VoteRequestDto {
    private String associateId;
    private VoteValue value;
}