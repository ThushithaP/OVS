package com.ovs.VotingSystem.service;

import com.ovs.VotingSystem.constatnt.Status;
import com.ovs.VotingSystem.dto.CandidateDto;
import com.ovs.VotingSystem.entity.Candidate;
import com.ovs.VotingSystem.repository.CandidateRepository;
import com.ovs.VotingSystem.utils.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CandidateServiceImpl implements CandidateService{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CandidateRepository candidateRepository;
    @Override
    public void addCandidate(CandidateDto candidateDto) throws Exception {
        Integer userId = (Integer) request.getAttribute("userId");
        String now = (String) request.getAttribute("currentTime");

        Candidate candidate = new Candidate();
        candidate.setFullName(candidateDto.getFullName());
        candidate.setFirstName(candidateDto.getFirstName());
        candidate.setLastName(candidateDto.getLastName());
        candidate.setNic(candidateDto.getNic());
        candidate.setMobile(candidateDto.getMobile());
        candidate.setDob(candidateDto.getDob());
        candidate.setPositionId(AESUtils.decrypt(candidateDto.getPositionId()));
        candidate.setCandidateStatus("A");
        candidate.setCreatedAt(now);
        candidate.setCreatedBy(userId);
        candidateRepository.save(candidate);
    }

    @Override
    public List<CandidateDto> getAllCandidates(String searchValue) throws Exception {
        List<Object[]> candidates ;
        if(searchValue != null && !searchValue.isEmpty()) {
            candidates = candidateRepository.findBySearchValue(searchValue);
        }else {
            candidates = candidateRepository.findRecords();
        }
        List<CandidateDto> candidateDtos = new ArrayList<>();
        for (Object[] data : candidates) {
            Candidate candidate = (Candidate) data[0];
            String positionName = (String) data[1];
            CandidateDto candidateDto = new CandidateDto();

            candidateDto.setFullName(candidate.getFullName());
            candidateDto.setFirstName(candidate.getFirstName());
            candidateDto.setLastName(candidate.getLastName());
            candidateDto.setNic(candidate.getNic());
            candidateDto.setDob(candidate.getDob());
            candidateDto.setMobile(candidate.getMobile());
            candidateDto.setCandidateStatus(Objects.requireNonNull(Status.getByCode(candidate.getCandidateStatus())).getValue());
            candidateDto.setPosition(positionName);
            candidateDto.setEncId(AESUtils.encrypt(candidate.getId()));
            candidateDto.setAction(getBtnText(candidate, candidateDto));

            candidateDtos.add(candidateDto);
        }
        return candidateDtos;
    }

    private static String getBtnText(Candidate candidate, CandidateDto candidateDto) {

        String btnText = "<a type=\"button\" data-placement=\"top\" href=\"javascript:editRecord('"+ candidateDto.getEncId() + "');\""+
                "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
                "title=\"Edit Candidate\"><i class=\"bi bi-pencil-fill\"></i></a>";

        if(Status.ACTIVE.getCode().equals(candidate.getCandidateStatus())) {
            btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:inactiveRecord('"+ candidateDto.getEncId()+ "');\""+
                    "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
                    "title=\"Inactive Candidate\"><i class=\"bi bi-x-circle-fill\"></i></a>";
        }
        if(Status.INACTIVE.getCode().equals(candidate.getCandidateStatus())) {
            btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:activeRecord('"+ candidateDto.getEncId() + "');\""+
                    "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\"" +
                    "title=\"Active Candidate\"><i class=\"bi bi-check-circle-fill\"></i></a>";
        }
        btnText += "<a type=\"button\" data-placement=\"top\" href=\"javascript:deleteRecord('" + candidateDto.getEncId() + "');\" " +
                "class=\"me-3 edit btn1 btn-outline-white btn-rounded btn-sm js-init-tooltip\" title=\"Delete Candidate\">" +
                "<i class=\"bi bi-trash-fill\"></i></a>";

        return btnText;
    }

    @Override
    public void changeStatus(Integer id, String status) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if(candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setCandidateStatus(status);
            candidateRepository.save(candidate);
        } else {
            throw new NoSuchElementException("Candidate Not Found");
        }
    }

    @Override
    public Optional<Candidate> read(Integer id) {
        return candidateRepository.findById(id);
    }

    @Override
    public void updateCandidate(CandidateDto candidateDto, Integer id) {
        Integer userId = (Integer) request.getAttribute("userId");
        String now = (String) request.getAttribute("currentTime");
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setFullName(candidateDto.getFullName());
            candidate.setFirstName(candidateDto.getFirstName());;
            candidate.setLastName(candidateDto.getLastName());;
            candidate.setNic(candidateDto.getNic());;
            candidate.setMobile(candidateDto.getMobile());;
            candidate.setCandidateStatus(candidateDto.getCandidateStatus());;
            candidate.setUpdatedBy(userId);
            candidate.setLmd(now);
            candidateRepository.save(candidate);

        } else {
            throw new NoSuchElementException("Candidate Not Found");
        }
    }

    @Override
    public List<CandidateDto> allCandidate() throws Exception {

        List<Candidate> candidates = candidateRepository.findActiveCandidate();
       List<CandidateDto> candidateDtoList = new ArrayList<>();
       for (Candidate candidate : candidates) {
           CandidateDto candidateDto = new CandidateDto();
           candidateDto.setFullName(candidate.getCandidateStatus()+ " " + candidate.getLastName());
           candidateDto.setEncId(AESUtils.encrypt(candidate.getId()));

           candidateDtoList.add(candidateDto);
       }
       return candidateDtoList;
    }

    @Override
    public List<CandidateDto>candidateByPositionId(Integer id) throws Exception {

        List<Candidate> candidates = candidateRepository.findByPositionId(id);
        List<CandidateDto> candidateDtoList = new ArrayList<>();
        for (Candidate candidate : candidates) {
            CandidateDto candidateDto = new CandidateDto();
            candidateDto.setFullName(candidate.getFirstName()+ " " + candidate.getLastName());
            candidateDto.setEncId(AESUtils.encrypt(candidate.getId()));

            candidateDtoList.add(candidateDto);
        }
        return candidateDtoList;
    }
}
