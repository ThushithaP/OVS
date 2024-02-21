package com.ovs.VotingSystem.controller;

import com.ovs.VotingSystem.dto.CandidateDto;
import com.ovs.VotingSystem.dto.PositionDto;
import com.ovs.VotingSystem.entity.Candidate;
import com.ovs.VotingSystem.entity.Position;
import com.ovs.VotingSystem.service.CandidateService;
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
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final PositionService positionService;

    @Autowired
    private final CandidateService candidateService;

    @GetMapping("/index")
    public String index(Model model) throws Exception {
        List<PositionDto> positionList = positionService.getActivePosition();
        System.out.println(positionList);
        model.addAttribute("positions",positionList);
        return "candidates/index";
    }

    @PostMapping("/save")
    public ResponseEntity<String> addCandidate(@Validated CandidateDto candidateDto) throws Exception {
        candidateService.addCandidate(candidateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Candidate Created Successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam("search[value]") String searchValue) throws Exception {
        List<CandidateDto> candidates = candidateService.getAllCandidates(searchValue);

        int totalRecords = candidates.size();

        Map<String, Object> resData = new HashMap<>();
        resData.put("data", candidates);
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
        String message = "Candidate Updated Successfully";;
        if(action.equals("active")) {
            status = "A";
        } else if (action.equals("inactive")) {
            status = "I";
        } else if (action.equals("delete")) {
            status = "D";
            message = "Candidate Deleted Successfully";
        }

        try {
            candidateService.changeStatus(id, status);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate Not Found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update candidate status");
        }

    }

    @PostMapping("/read")
    public ResponseEntity<?> read(HttpServletRequest request) throws Exception {
        try {
            String encId = request.getParameter("id");
            System.out.println("encId" + encId);
            Integer id = AESUtils.decrypt(encId);

            Optional<Candidate> candidateOptional = candidateService.read(id);
            if (candidateOptional.isPresent()) {
                Candidate candidate = candidateOptional.get();
                return ResponseEntity.status(HttpStatus.OK).body(candidate);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate Not Found");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown Error Occurred.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(HttpServletRequest request, @Validated CandidateDto candidateDto) throws Exception {
        String encId = request.getParameter("encID");
        Integer id = AESUtils.decrypt(encId);
        try {
            candidateService.updateCandidate(candidateDto,id);
            return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate Not Found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update candidate status");
        }
    }
}
