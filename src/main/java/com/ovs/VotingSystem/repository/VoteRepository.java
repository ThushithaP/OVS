package com.ovs.VotingSystem.repository;

import com.ovs.VotingSystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface VoteRepository extends JpaRepository<Vote,Integer> {

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN true ELSE false END FROM Vote v WHERE v.positionId = :pId AND v.candidateId = :cId AND v.votedBy = :vId")
     Boolean existsByPositionIdAndCandidateIdAndVotedBy(Integer pId, Integer cId, Integer vId);

    @Query("SELECT p.positionName, " +
            "GROUP_CONCAT(CONCAT(c.firstName, ' ', c.lastName)) AS cname, " +
            "COUNT(c.id) AS candidate_count " +
            "FROM Position p " +
            "LEFT JOIN Candidate c on c.positionId = p.id " +
            "WHERE c.candidateStatus = 'A' AND p.positionStatus = 'A' " +
            "GROUP BY p.id")
    List<Object[]> getPositionsWithCandidatesCount();

    @Query("SELECT " +
            "p.positionName AS position_name, " +
            "CONCAT(c.firstName, ' ', c.lastName) AS candidate_name, " +
            "COUNT(DISTINCT v.id) AS vote_count " +
            "FROM " +
            "Vote v " +
            "INNER JOIN Candidate c ON c.id = v.candidateId " +
            "INNER JOIN Position p ON p.id = v.positionId " +
            "GROUP BY " +
            "p.positionName, CONCAT(c.firstName, ' ', c.lastName)")
    List<Object[]> getCandidateVoteByPosition();

    @Query("SELECT CASE WHEN p.maxVote = (SELECT COUNT(v.id) FROM Vote v WHERE v.votedBy = :userId AND v.positionId = :positionId) " +
            "THEN true ELSE false END FROM Position p WHERE p.id = :positionId")
    Boolean isVoteLimitReached(Integer userId, Integer positionId);




}
