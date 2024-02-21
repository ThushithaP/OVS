package com.ovs.VotingSystem.controller;

import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Position;
import com.ovs.VotingSystem.service.PositionService;
import com.ovs.VotingSystem.utils.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/position")
@RequiredArgsConstructor
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping("/index")
    public String index(HttpServletRequest request,Model model) {
        return "position/index_page";
    }

    @PostMapping("/save")
    public ResponseEntity<String> addPosition(@Validated PositionDto positionDto) {
        positionService.addPosition(positionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Position Created Successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam("search[value]") String searchValue) throws Exception {
        List<PositionDto> positions = positionService.getAllPositions(searchValue);

        int totalRecords = positions.size();

        Map<String, Object> resData = new HashMap<>();
        resData.put("data", positions);
        resData.put("recordsTotal", totalRecords);
        resData.put("recordsFiltered", totalRecords);

        return ResponseEntity.ok().body(resData);
    }

    @PostMapping("/status")
    public ResponseEntity<String> changeStatus(HttpServletRequest request) throws Exception {
        String encId = request.getParameter("id");
        Integer id = AESUtils.decrypt(encId);
        String action = request.getParameter("act");
        String status = null;
        String message = "Position Updated Successfully";;
        if(action.equals("active")) {
            status = "A";
        } else if (action.equals("inactive")) {
            status = "I";
        } else if (action.equals("delete")) {
            status = "D";
            message = "Position Deleted Successfully";
        }

        try {
            positionService.changeStatus(id, status);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position Not Found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update position status");
        }

    }

    @PostMapping("/read")
    public ResponseEntity<?> read(HttpServletRequest request) throws Exception {
        try {
            String encId = request.getParameter("id");
            System.out.println("encId" + encId);
            Integer id = AESUtils.decrypt(encId);

            Optional<Position> positionOptional = positionService.read(id);
            if (positionOptional.isPresent()) {
                Position position = positionOptional.get();
                return ResponseEntity.status(HttpStatus.OK).body(position);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position Not Found");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown Error Occurred.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(HttpServletRequest request, @Validated PositionDto positionDto) throws Exception {
        String encId = request.getParameter("encID");
        Integer id = AESUtils.decrypt(encId);
        try {
            positionService.updatePosition(positionDto,id);
            return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position Not Found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update position status");
        }
    }
}
