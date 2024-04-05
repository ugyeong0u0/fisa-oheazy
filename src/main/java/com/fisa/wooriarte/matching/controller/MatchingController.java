package com.fisa.wooriarte.matching.controller;

import com.fisa.wooriarte.matching.DTO.MatchingDTO;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class MatchingController {

    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/admin/matchings")
    public String getAllMatching() {
        return matchingService.getAllMatching().toString();
    }

    @GetMapping("/admin/matchings/{id}")
    public String getMatching(@PathVariable(value = "id") Long id) {
        return matchingService.getMatching(id).toString();
    }

    @PatchMapping("/admin/matchings/{id}")
    public String updateMatching(@PathVariable(value = "id") Long id, @RequestBody MatchingDTO matchingDTO) {
        if(matchingService.updateMatching(id, matchingDTO))
            return "success";
        return "fail";
    }

    // 공간 대여자 -> 프로젝트 매니저 매칭 신청
    @RequestMapping("/space/{id}/request")
    public String addMatchingBySpaceRental(@PathVariable(value = "id") Long spaceId,  @RequestBody Long projectId) {
        return matchingService.addMatchingBySpaceRental(spaceId, projectId).toString();
    }

    // 프로젝트 매니저 -> 공간 대여자 매칭 신청
    @RequestMapping("/project/{id}/request")
    public String addMatchingByProjectManager(@PathVariable(value = "id") Long projectId,  @RequestBody Long spaceId) {
        return matchingService.addMatchingByProjectManager(projectId, spaceId).toString();
    }

    // 매칭 수락, 거절
    @PostMapping("/matching/{matching_id}")
    public String approvalMatching(@PathVariable(value = "matching_id") Long id, @RequestBody boolean accept) {
        if(matchingService.approvalMatching(id, accept ? MatchingStatus.PREPARING : MatchingStatus.CANCEL)) {
            return "success";
        }
        return "fail";
    }

    // 공간 대여자 대기중인 매칭 조회
    @GetMapping("/space-rental/{id}/waitings")
    public String findSpaceRentalWaitingMatching(@PathVariable(value = "id") String id) {
        return matchingService.findSpaceRentalWaitingMatching(id).toString();
    }

    // 공간 대여자 받은 매칭 조회
    @GetMapping("/space-rental/{id}/offers")
    public String findSpaceRentalOfferMatching(@PathVariable(value = "id") String id) {
        return matchingService.findSpaceRentalOfferMatching(id).toString();
    }

    // 공간 대여자 상사된 매칭 조회
    @GetMapping("/space-rental/{id}/success")
    public String findSpaceRentalSuccessMatching(@PathVariable(value = "id") String id) {
        return matchingService.findSpaceRentalSuccessMatching(id).toString();
    }

    // 프로젝트 매니저 대기중인 매칭 조회
    @GetMapping("/project-managers/{id}/waitings")
    public String findProjectManagerWaitingMatching(@PathVariable(value = "id") String id) {
        return matchingService.findProjectManagerWaitingMatching(id).toString();
    }

    // 프로젝트 매니저 받은 매칭 조회
    @GetMapping("/project-managers/{id}/offers")
    public String findProjectManagerOfferMatching(@PathVariable(value = "id") String id) {
        return matchingService.findProjectManagerOfferMatching(id).toString();
    }

    // 프로젝트 매니저 성사된 매칭 조회
    @GetMapping("/project-managers/{id}/success")
    public String findProjectManagerSuccessMatching(@PathVariable(value = "id") String id) {
        return matchingService.findProjectManagerSuccessMatching(id).toString();
    }
}