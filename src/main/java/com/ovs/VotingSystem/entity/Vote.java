package com.ovs.VotingSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "candidate_id")
    private Integer candidateId;
    @Column(name = "position_id")
    private Integer positionId;
    @Column(name="voted_at")
    private String votedAt;
    @Column(name="voted_by")
    private Integer votedBy;
}
