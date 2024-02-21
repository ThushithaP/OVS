package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.dto.CandidateDto;
import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Candidate;
import com.ovs.VotingSystem.entity.Position;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    void addCandidate(CandidateDto candidateDto) throws Exception;
    List<CandidateDto> getAllCandidates(String searchValue) throws Exception;

    void changeStatus(Integer id, String status);

    Optional<Candidate> read(Integer id);

    void updateCandidate(CandidateDto candidateDto, Integer id);

    List<CandidateDto> allCandidate() throws Exception;

    List<CandidateDto> candidateByPositionId(Integer id) throws Exception;

}
