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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final PositionService positionService;
    private final CandidateService candidateService;

    @GetMapping("/index")
    public String index(Model model) throws Exception {
        List<PositionDto> positionList = positionService.getActivePosition();
        model.addAttribute("positions",positionList);
        return "vote/index";
    }

    @PostMapping("/candidate")
    public ResponseEntity<?> getCandidate(HttpServletRequest request) throws Exception {
        String encId = request.getParameter("id");
        Integer positionId = AESUtils.decrypt(encId);
        List<CandidateDto> candidateDtoList = candidateService.candidateByPositionId(positionId);
        return ResponseEntity.status(HttpStatus.OK).body(candidateDtoList);
    }


}
