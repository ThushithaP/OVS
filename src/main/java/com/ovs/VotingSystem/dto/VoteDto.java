package com.ovs.VotingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoteDto {

    @NotBlank
    private String positionId;
    @NotBlank
    private String candidateId;
}
