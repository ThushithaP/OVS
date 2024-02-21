package com.ovs.VotingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateDto {

    private Integer id;
    private String encId;
    private String action;
    private String candidateStatus;
    private String position;

    @NotBlank
    private String fullName;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String nic;
    @NotBlank
    private String mobile;
    @NotBlank
    private String dob;
    @NotNull
    private String positionId;

}
