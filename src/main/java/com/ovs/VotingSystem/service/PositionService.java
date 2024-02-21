package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Position;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    void addPosition(PositionDto positionDto);
    List<PositionDto> getAllPositions(String searchValue) throws Exception;

    void changeStatus(Integer id, String status);

    Optional<Position> read(Integer id);

    void updatePosition(PositionDto positionDto, Integer id);


    List<PositionDto> getActivePosition() throws Exception;
}
