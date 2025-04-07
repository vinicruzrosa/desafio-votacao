package com.desafio.votacao.client;

import com.desafio.votacao.Data.Dtos.ProposalStatusResponse;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class ProposalValidatorClient {

    private final Random random = new Random();
    public ProposalStatusResponse validateProposal() {
        boolean aprovado = random.nextBoolean();
        return new ProposalStatusResponse(aprovado ? "APPROVED" : "DECLINED");
    }
}
