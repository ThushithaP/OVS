package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.dto.VoteDto;

public interface VoteService {

    void addVote(VoteDto voteDto) throws Exception;

    boolean checkAlreadyVoted(VoteDto voteDto) throws Exception;

    boolean voteLimitCheck(VoteDto voteDto) throws Exception;
}
