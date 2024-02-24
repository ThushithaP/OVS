package com.ovs.VotingSystem.controller;

import com.ovs.VotingSystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/index")
    public String index(Model model) {
        List<Object[]> candidatesByPositions = dashboardService.getCandidateWithPosition();
        List<Object[]> candidateVotesMap  = dashboardService.voteCountByCandidate();
        Map<String, List<Object[]>> votesByPosition = new LinkedHashMap<>();
        for (Object[] row : candidateVotesMap) {
            String positionName = (String) row[0];
            String candidateName = (String) row[1];
            Long voteCount = (Long) row[2];

            if (!votesByPosition.containsKey(positionName)) {
                votesByPosition.put(positionName, new ArrayList<>());
            }
            votesByPosition.get(positionName).add(new Object[]{candidateName, voteCount});
        }
        
        Comparator<Object[]> candidateVoteComparator = Comparator.comparingLong(candidate -> (Long) candidate[1]);

        for (List<Object[]> candidateVotes : votesByPosition.values()) {
            candidateVotes.sort(candidateVoteComparator.reversed());
        }

        model.addAttribute("candidatesByPositions",candidatesByPositions);
        model.addAttribute("votesByPosition",votesByPosition);
        return "/dashboard/index";
    }
}
