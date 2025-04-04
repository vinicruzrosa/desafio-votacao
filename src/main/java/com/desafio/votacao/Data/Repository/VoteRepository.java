package com.desafio.votacao.Data.Repository;


import com.desafio.votacao.Data.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByAssociateIdAndProposalId(String associateId, Long proposalId);
}