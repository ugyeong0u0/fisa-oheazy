package com.fisa.wooriarte.matching.controller;

import com.fisa.wooriarte.matching.dto.MatchingDto;
import com.fisa.wooriarte.matching.dto.MatchingResponseDto;
import com.fisa.wooriarte.matching.dto.MatchingStatusDto;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.service.MatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j // Lombok 라이브러리를 사용하여 로그 객체를 자동으로 생성합니다.
@RequestMapping("/api/matchings")
@RestController
public class MatchingController {

    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    // 모든 매칭 가져오기
    @GetMapping("/admin")
    public ResponseEntity<List<MatchingDto>> getAllMatching() {
        try {
            log.info("모든 매칭을 조회합니다");
            List<MatchingDto> matchingList = matchingService.getAllMatching();
            return ResponseEntity.ok(matchingList);
        } catch (Exception e) {
            log.error("모든 매칭 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // 특정 매칭 조회
    @GetMapping("/admin/matchings/{matching-id}")
    public ResponseEntity<MatchingDto> getMatching(@PathVariable(name = "matching-id") Long matchingId) {
        try {
            log.info("ID가 {}인 매칭을 조회합니다", matchingId);
            MatchingDto matchingDto = matchingService.getMatching(matchingId);
            return ResponseEntity.ok(matchingDto);
        } catch (NoSuchElementException e) {
            log.error("ID가 {}인 매칭을 찾을 수 없습니다: {}", matchingId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("ID가 {}인 매칭 조회 중 오류 발생: {}", matchingId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // 매칭 상태 업데이트
    @PatchMapping("/admin/matchings/{matching-id}")
    public ResponseEntity<String> updateMatching(@PathVariable(name = "matching-id") Long matchingId, @RequestBody MatchingStatusDto matchingStatusDTO) {
        try {
            log.info("ID가 {}인 매칭 상태를 업데이트합니다", matchingId);
            MatchingStatus matchingStatus = matchingStatusDTO.getMatchingStatus();
            if (matchingService.updateMatching(matchingId, matchingStatus)) {
                log.info("ID가 {}인 매칭 상태 업데이트 성공", matchingId);
                return ResponseEntity.ok("매칭 상태가 성공적으로 업데이트되었습니다.");
            } else {
                log.error("ID가 {}인 매칭 상태 업데이트 실패", matchingId);
                return ResponseEntity.badRequest().body("매칭 상태 업데이트에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("ID가 {}인 매칭 상태 업데이트 중 오류 발생: {}", matchingId, e.getMessage());
            return ResponseEntity.internalServerError().body("매칭 상태 업데이트 중 오류가 발생했습니다.");
        }
    }

    // 프로젝트 매니저가 매칭 요청
    @PostMapping("/project/{project-item-id}/request")
    public ResponseEntity<String> addMatchingByProjectManager(@PathVariable(name = "project-item-id") Long projectItemId, @RequestBody Map<String, Long> spaceItemIdInfo) {
        try {
            log.info("프로젝트 매니저가 프로젝트 ID: {}, 공간 아이템 ID: {}로 매칭 요청", projectItemId, spaceItemIdInfo.get("spaceItemId"));

            Long spaceItemId = spaceItemIdInfo.get("spaceItemId");

            MatchingDto matchingDto = matchingService.addMatchingByProjectManager(projectItemId, spaceItemId);

            if (matchingDto != null) {
                log.info("프로젝트 ID: {}에 대한 매칭 추가 성공", projectItemId);
                return ResponseEntity.ok("프로젝트 ID " + projectItemId + "에 대한 매칭이 성공적으로 추가되었습니다.");
            } else {
                log.error("프로젝트 ID: {}에 대한 매칭 추가 실패", projectItemId);
                return ResponseEntity.badRequest().body("프로젝트 ID " + projectItemId + "에 대한 매칭 추가 실패");
            }
        } catch (Exception e) {
            log.error("프로젝트 ID: {}에 대한 매칭 추가 중 오류 발생: {}", projectItemId, e.getMessage());
            return ResponseEntity.internalServerError().body("매칭 추가 중 오류가 발생했습니다.");
        }
    }

    // 임대사업자가 매칭 요청
    @PostMapping("/space/{space-item-id}/request")
    public ResponseEntity<String> addMatchingBySpaceRental(@PathVariable(name = "space-item-id") Long spaceItemId, @RequestBody Map<String, Long> projectItemIdInfo) {
        try {
            log.info("임대사업자가 공간 아이템 ID: {}로 매칭 요청", spaceItemId);
            Long projectItemId = projectItemIdInfo.get("projectItemId");

            MatchingDto matchingDto = matchingService.addMatchingBySpaceRental(spaceItemId, projectItemId);

            if (matchingDto != null) {
                log.info("공간 아이템 ID: {}에 대한 매칭 추가 성공", spaceItemId);
                return ResponseEntity.ok("공간 아이템 ID " + spaceItemId + "에 대한 매칭이 성공적으로 추가되었습니다.");
            } else {
                log.error("공간 아이템 ID: {}에 대한 매칭 추가 실패", spaceItemId);
                return ResponseEntity.badRequest().body("공간 아이템 ID " + spaceItemId + "에 대한 매칭 추가 실패");
            }
        } catch (Exception e) {
            log.error("공간 아이템 ID: {}에 대한 매칭 추가 중 오류 발생: {}", spaceItemId, e.getMessage());
            return ResponseEntity.internalServerError().body("매칭 추가 중 오류가 발생했습니다.");
        }
    }

    // 매칭 승인 상태 업데이트
    @PostMapping("/{matching-id}")
    public ResponseEntity<String> approvalMatching(@PathVariable(name = "matching-id") Long id, @RequestBody Map<String, Boolean> accept) {
        try {
            log.info("매칭 ID: {}의 승인 상태 업데이트 요청, 승인 여부: {}", id, accept);
            boolean boolAccept = accept.getOrDefault("accept", false);
            if (matchingService.updateMatching(id, boolAccept ? MatchingStatus.WAITING : MatchingStatus.CANCEL)) {
                log.info("매칭 ID: {}의 승인 상태 업데이트 성공", id);
                return ResponseEntity.ok("매칭 상태가 성공적으로 업데이트되었습니다.");
            }
            log.error("매칭 ID: {}의 승인 상태 업데이트 실패", id);
            return ResponseEntity.badRequest().body("매칭 상태 업데이트에 실패했습니다.");
        } catch (Exception e) {
            log.error("매칭 ID: {}의 승인 상태 업데이트 중 오류 발생: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("매칭 상태 업데이트 중 오류가 발생했습니다.");
        }
    }


    // 대기 중인 공간 대여자의 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/waitings")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalWaitingMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        try {
            log.info("공간 대여자 ID: {}의 대기 중인 매칭을 조회합니다", spaceRentalId);
            List<MatchingResponseDto> waitingMatchings = matchingService.findSpaceRentalWaitingMatching(spaceRentalId);
            return ResponseEntity.ok(waitingMatchings);
        } catch (NoSuchElementException e) {
            log.error("대기 중인 매칭을 찾을 수 없음 - ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("대기 중인 매칭 조회 중 오류 발생 - ID: {}", spaceRentalId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 공간 대여자가 받은 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/offers")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalOfferMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        try {
            log.info("공간 대여자 ID: {}의 받은 매칭을 조회합니다", spaceRentalId);
            List<MatchingResponseDto> offerMatchings = matchingService.findSpaceRentalOfferMatching(spaceRentalId);
            return ResponseEntity.ok(offerMatchings);
        } catch (NoSuchElementException e) {
            log.error("받은 매칭을 찾을 수 없음 - ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("받은 매칭 조회 중 오류 발생 - ID: {}", spaceRentalId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 성사된 임대사업자의 매칭 조회
    @GetMapping("/space-rental/{space-rental-id}/success")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalSuccessMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        try {
            log.info("임대사업자 ID: {}의 성사된 매칭을 조회합니다", spaceRentalId);
            List<MatchingResponseDto> successMatchings = matchingService.findSpaceRentalSuccessMatching(spaceRentalId);
            return ResponseEntity.ok(successMatchings);
        } catch (NoSuchElementException e) {
            log.error("성사된 매칭을 찾을 수 없음 - ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("성사된 매칭 조회 중 오류 발생 - ID: {}", spaceRentalId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 대기 중인 프로젝트 매니저의 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/waitings")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerWaitingMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        try {
            log.info("프로젝트 매니저 ID: {}의 대기 중인 매칭을 조회합니다", projectManagerId);
            List<MatchingResponseDto> waitingMatchings = matchingService.findProjectManagerWaitingMatching(projectManagerId);
            return ResponseEntity.ok(waitingMatchings);
        } catch (NoSuchElementException e) {
            log.error("대기 중인 매칭을 찾을 수 없음 - ID: {}", projectManagerId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("대기 중인 매칭 조회 중 오류 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 매니저가 받은 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/offers")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerOfferMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        try {
            log.info("프로젝트 매니저 ID: {}의 받은 매칭을 조회합니다", projectManagerId);
            List<MatchingResponseDto> offerMatchings = matchingService.findProjectManagerOfferMatching(projectManagerId);
            return ResponseEntity.ok(offerMatchings);
        } catch (NoSuchElementException e) {
            log.error("받은 매칭을 찾을 수 없음 - ID: {}", projectManagerId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("받은 매칭 조회 중 오류 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 성사된 프로젝트 매니저의 매칭 조회
    @GetMapping("/project-managers/{project-manager-id}/success")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerSuccessMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        try {
            log.info("프로젝트 매니저 ID: {}의 성사된 매칭을 조회합니다", projectManagerId);
            List<MatchingResponseDto> successMatchings = matchingService.findProjectManagerSuccessMatching(projectManagerId);
            return ResponseEntity.ok(successMatchings);
        } catch (NoSuchElementException e) {
            log.error("성사된 매칭을 찾을 수 없음 - ID: {}", projectManagerId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("성사된 매칭 조회 중 오류 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
