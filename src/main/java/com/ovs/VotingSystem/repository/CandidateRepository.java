package com.ovs.VotingSystem.repository;

import com.ovs.VotingSystem.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c, p.positionName FROM Candidate c JOIN Position p ON c.positionId = p.id WHERE c.candidateStatus IN ('A', 'I')")
    List<Object[]> findRecords();

    @Query("SELECT c, p.positionName FROM Candidate c INNER JOIN Position p ON c.positionId = p.id  " +
            "WHERE c.candidateStatus IN ('A', 'I') AND c.fullName LIKE %:searchValue% OR " +
            "c.firstName LIKE %:searchValue% OR " +
            "c.lastName LIKE %:searchValue% OR " +
            "c.nic LIKE %:searchValue% OR " +
            "c.mobile LIKE %:searchValue%")
    List<Object[]> findBySearchValue( String searchValue);

    @Query("SELECT c FROM Candidate c WHERE c.candidateStatus ='A'")
    List<Candidate> findActiveCandidate();

    @Query("SELECT c FROM Candidate c WHERE c.candidateStatus ='A' AND c.positionId = :id")
    List<Candidate> findByPositionId(Integer id);

}
