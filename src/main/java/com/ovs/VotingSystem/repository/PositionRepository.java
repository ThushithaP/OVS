package com.ovs.VotingSystem.repository;

import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Long> {

    @Query("SELECT p FROM Position p WHERE p.positionStatus IN ('A', 'I')")
    List<Position> findRecords();
    List<Position> findByPositionNameContainingOrPositionStatusContaining(String positionName, String positionStatus);

    @Query("SELECT p FROM Position p WHERE p.positionStatus ='A'")
    List<Position> findActivePosition();

}
