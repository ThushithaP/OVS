package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.config.JwtService;
import com.ovs.VotingSystem.constatnt.Status;
import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Position;
import com.ovs.VotingSystem.repository.PositionRepository;
import com.ovs.VotingSystem.utils.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PositionServiceImpl implements PositionService{
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private HttpServletRequest request;

    private JwtService jwtService;

    @Override
    public void addPosition( PositionDto positionDto) {
        Integer userId = (Integer) request.getAttribute("userId");
        String now = (String) request.getAttribute("currentTime");

        Position position = new Position();
        position.setPositionName(positionDto.getPositionName());
        position.setPositionStatus(positionDto.getPositionStatus());
        position.setMaxVote(positionDto.getMaxVote());
        position.setCreatedAt(now);
        position.setCreatedBy(userId);
        positionRepository.save(position);
    }

    @Override
    public List<PositionDto> getAllPositions(String searchValue) throws Exception {
        List<Position> positions ;
        if(searchValue != null && !searchValue.isEmpty()) {
            positions = positionRepository.findByPositionNameContainingOrPositionStatusContaining(searchValue, searchValue);
        }else {
            positions = positionRepository.findRecords();
        }
        List<PositionDto> positionDtos = new ArrayList<>();
        for (Position position : positions) {
            PositionDto positionDto = new PositionDto();

            positionDto.setPositionName(position.getPositionName());
            positionDto.setPositionStatus(Objects.requireNonNull(Status.getByCode(position.getPositionStatus())).getValue());
            positionDto.setMaxVote(position.getMaxVote());
            positionDto.setEncId(AESUtils.encrypt(position.getId()));
            positionDto.setAction(getBtnText(position, positionDto));

            positionDtos.add(positionDto);
        }
        return positionDtos;
    }

    private static String getBtnText(Position position, PositionDto positionDto) {

        String btnText = "<a type=\"button\" data-placement=\"top\" href=\"javascript:editRecord('"+ positionDto.getEncId() + "');\""+
                "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
                "title=\"Edit Position\"><i class=\"bi bi-pencil-fill\"></i></a>";

        if(Status.ACTIVE.getCode().equals(position.getPositionStatus())) {
            btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:inactiveRecord('"+ positionDto.getEncId()+ "');\""+
            "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
            "title=\"Inactive Position\"><i class=\"bi bi-x-circle-fill\"></i></a>";
        }
        if(Status.INACTIVE.getCode().equals(position.getPositionStatus())) {
            btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:activeRecord('"+ positionDto.getEncId() + "');\""+
                    "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
                    "title=\"Active Position\"><i class=\"bi bi-check-circle-fill\"></i></a>";
        }
        btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:deleteRecord('" + positionDto.getEncId() + "');\" " +
                "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\" title=\"Delete Position\">" +
                "<i class=\"bi bi-trash-fill\"></i></a>";

        return btnText;
    }

    @Override
    public void changeStatus(Integer id, String status) {
        Optional<Position> positionOptional = positionRepository.findById(Long.valueOf(id));
        if(positionOptional.isPresent()) {
            Position position = positionOptional.get();
            position.setPositionStatus(status);
            positionRepository.save(position);
        } else {
            throw new NoSuchElementException("Position Not Found");
        }
    }

    @Override
    public Optional<Position> read(Integer id) {
        return positionRepository.findById(Long.valueOf(id));
    }

    @Override
    public void updatePosition(PositionDto positionDto, Integer id) {
        Integer userId = (Integer) request.getAttribute("userId");
        String now = (String) request.getAttribute("currentTime");
        Optional<Position> positionOptional = positionRepository.findById(Long.valueOf(id));
        if (positionOptional.isPresent()) {
            Position position = positionOptional.get();
            position.setPositionName(positionDto.getPositionName());
            position.setPositionStatus(positionDto.getPositionStatus());
            position.setMaxVote(positionDto.getMaxVote());
            position.setUpdatedBy(userId);
            position.setLmd(now);
            positionRepository.save(position);

        } else {
            throw new NoSuchElementException("Position Not Found");
        }
    }

    @Override
    public List<PositionDto> getActivePosition() throws Exception {
        List<Position> positionList =  positionRepository.findActivePosition();
        List<PositionDto> positionDtoList = new ArrayList<>();
        for (Position position : positionList) {
            PositionDto positionDto = new PositionDto();
            positionDto.setPositionName(position.getPositionName());
            positionDto.setEncId(AESUtils.encrypt(position.getId()));

            positionDtoList.add(positionDto);
        }
        return positionDtoList;
    }
}
