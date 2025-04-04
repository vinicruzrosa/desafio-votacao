package com.desafio.votacao.Data.Entity;


import com.desafio.votacao.Data.Enums.VoteValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String associateId;

    private VoteValue voteValue;

    @ManyToOne
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;
}
