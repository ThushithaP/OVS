package com.ovs.VotingSystem.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Entity
@Data
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="full_name")
    private String fullName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String nic;
    private String mobile;
    private String dob;

    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "status")
    private String candidateStatus;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    private String lmd;

    @Column(name = "updated_by")
    private Integer updatedBy;


}
