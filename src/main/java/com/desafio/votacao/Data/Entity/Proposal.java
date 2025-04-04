package com.desafio.votacao.Data.Entity;

import com.desafio.votacao.Data.Enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 200)
    private String description;
    private String creator;
    private StatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime startVotation;
    private LocalDateTime endVotation;
    private Integer yesVotes;
    private Integer noVotes;
    private String result;
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private List<Vote> votes;
}
