package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.dto.VoteDto;
import com.ovs.VotingSystem.entity.Vote;
import com.ovs.VotingSystem.repository.VoteRepository;
import com.ovs.VotingSystem.utils.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public void addVote(VoteDto voteDto) throws Exception {
        Integer userId = (Integer) request.getAttribute("userId");
        String now = (String) request.getAttribute("currentTime");

        Vote vote = new Vote();
        vote.setCandidateId(AESUtils.decrypt(voteDto.getCandidateId()));
        vote.setPositionId(AESUtils.decrypt(voteDto.getPositionId()));
        vote.setVotedAt(now);
        vote.setVotedBy(userId);
        voteRepository.save(vote);
    }
    @Override
    public boolean checkAlreadyVoted(VoteDto voteDto) throws Exception{
        Integer userId = (Integer) request.getAttribute("userId");
        Integer candidateId =  AESUtils.decrypt(voteDto.getCandidateId());
        Integer positionId =  AESUtils.decrypt(voteDto.getPositionId());

        return voteRepository.existsByPositionIdAndCandidateIdAndVotedBy(positionId,candidateId,userId);
    }
    @Override
    public boolean voteLimitCheck(VoteDto voteDto) throws Exception {
        Integer userId = (Integer) request.getAttribute("userId");
        Integer positionId =  AESUtils.decrypt(voteDto.getPositionId());

        return voteRepository.isVoteLimitReached(userId,positionId);
    }
}
