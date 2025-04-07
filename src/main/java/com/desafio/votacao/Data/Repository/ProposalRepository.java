package com.desafio.votacao.Data.Repository;

import com.desafio.votacao.Data.Entity.Proposal;
import com.desafio.votacao.Data.Enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByTitleIgnoreCaseAndStatus(String title, StatusEnum status);
}
