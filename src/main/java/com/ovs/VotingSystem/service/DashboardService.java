package com.ovs.VotingSystem.service;

import java.util.List;

public interface DashboardService {

    List<Object[]> getCandidateWithPosition();
    List<Object[]> voteCountByCandidate();
}
