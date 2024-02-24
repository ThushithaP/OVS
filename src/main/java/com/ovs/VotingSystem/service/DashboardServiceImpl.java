package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService{

    private final VoteRepository voteRepository;

    public DashboardServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }


    public List<Object[]> getCandidateWithPosition() {
        return voteRepository.getPositionsWithCandidatesCount();
    }
    public List<Object[]> voteCountByCandidate() {
        return voteRepository.getCandidateVoteByPosition();
    }
}
