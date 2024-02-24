package com.ovs.VotingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PositionDto {
    private Integer id;
    private String encId;
    private String lmd;
    private String action;

    @NotBlank
    private String positionName;
    @NotBlank
    private String positionStatus;
    @NotNull
    private Integer maxVote;
}
