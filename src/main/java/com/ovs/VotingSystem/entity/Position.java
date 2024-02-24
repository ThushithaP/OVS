package com.ovs.VotingSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String positionName;

    @Column(name = "status")
    private String positionStatus;

    @Column(name = "max_vote")
    private Integer maxVote;

    @Column(name="created_at")
    private String createdAt;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="lmd")
    private String lmd;

    @Column(name="updated_by")
    private Integer updatedBy;


}
