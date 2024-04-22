package com.fisa.wooriarte.matching.controller;

import com.fisa.wooriarte.matching.DTO.MatchingDTO;
import com.fisa.wooriarte.matching.DTO.MatchingStatusDTO;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MatchingController {

    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    // 모든 매칭 가져오기
    @GetMapping("/admin/matchings")
    public ResponseEntity<?> getAllMatching() {
        return new ResponseEntity<>(matchingService.getAllMatching(),HttpStatus.OK);

    }

    @GetMapping("/admin/matchings/{matching-id}")
    public ResponseEntity getMatching(@PathVariable(value = "matching-id") Long matchingId) {
        return new ResponseEntity<>(matchingService.getMatching(matchingId), HttpStatus.OK);
    }

    // 매칭 수정 (매칭에서는 상태만 수정가능)
    @PatchMapping("/admin/matchings/{matching-id}")
    public String updateMatching(@PathVariable(value = "matching-id") Long matchingId, @RequestBody MatchingStatusDTO matchingStatusDTO) {
        MatchingStatus matchingStatus = matchingStatusDTO.getMatchingStatus();
        if(matchingService.updateMatching(matchingId, matchingStatus))
            return "success";
        return "fail";
    }

    // 공간 대여자 -> 프로젝트 매니저 매칭 신청
    @PostMapping("/space/{space-item-id}/request")
    public ResponseEntity<?> addMatchingBySpaceRental(@PathVariable(value = "space-item-id") Long spaceItemId, @RequestBody Map<String, Long> projectItemIdInfo) {
        Long projectItemId = projectItemIdInfo.get("projectItemId");
        MatchingDTO resultDTO= matchingService.addMatchingBySpaceRental(spaceItemId, projectItemId);
        if(resultDTO!=null){
            return new ResponseEntity<>("매칭 성공", HttpStatus.OK);}
        else{
            return new ResponseEntity<>("매칭 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 프로젝트 매니저 -> 공간 대여자 매칭 신청
    @PostMapping("/project/{project-item-id}/request")
    public ResponseEntity<?> addMatchingByProjectManager(@PathVariable(value = "project-item-id") Long projectItemId, @RequestBody Map<String, Long> spaceItemIdInfo) {
        Long spaceItemId = spaceItemIdInfo.get("spaceItemId");

        MatchingDTO resultDTO=matchingService.addMatchingByProjectManager(projectItemId, spaceItemId);
        if(resultDTO!=null){
        return new ResponseEntity<>("매칭 성공", HttpStatus.OK);}
        else{
            return new ResponseEntity<>("매칭 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 매칭 수락, 거절
    @PostMapping("/matching/{matching-id}")
    public String approvalMatching(@PathVariable(value = "matching-id") Long id, @RequestBody String accept) {
        boolean boolAccept = Boolean.parseBoolean(accept);
        if(matchingService.updateMatching(id, boolAccept ? MatchingStatus.WAITING : MatchingStatus.CANCEL)) {
            return "success";
        }
        return "fail";
    }

    // 공간 대여자 대기중인 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/waitings")
    public List<MatchingDTO> findSpaceRentalWaitingMatching(@PathVariable(value = "space-rental-id") Long spaceRentalId) {
        return matchingService.findSpaceRentalWaitingMatching(spaceRentalId);
    }

    // 공간 대여자 받은 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/offers")
    public List<MatchingDTO> findSpaceRentalOfferMatching(@PathVariable(value = "space-rental-id") Long spaceRentalId) {
        return matchingService.findSpaceRentalOfferMatching(spaceRentalId);
    }

    // 공간 대여자 상사된 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/success")
    public List<MatchingDTO> findSpaceRentalSuccessMatching(@PathVariable(value = "space-rental-id") Long spaceRentalId) {
        return matchingService.findSpaceRentalSuccessMatching(spaceRentalId);
    }

    // 프로젝트 매니저 대기중인 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/waitings")
    public List<MatchingDTO> findProjectManagerWaitingMatching(@PathVariable(value = "project-manager-id") Long projectManagerId) {
        return matchingService.findProjectManagerWaitingMatching(projectManagerId);
    }

    // 프로젝트 매니저 받은 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/offers")
    public List<MatchingDTO> findProjectManagerOfferMatching(@PathVariable(value = "project-manager-id") Long projectManagerId) {
        return matchingService.findProjectManagerOfferMatching(projectManagerId);
    }

    // 프로젝트 매니저 성사된 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/success")
    public List<MatchingDTO> findProjectManagerSuccessMatching(@PathVariable(value = "project-manager-id") Long projectManagerId) {
        return matchingService.findProjectManagerSuccessMatching(projectManagerId);
    }
}