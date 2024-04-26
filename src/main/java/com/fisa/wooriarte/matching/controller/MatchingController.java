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

@Slf4j // Lombok 라이브러리를 사용하여 로그 객체를 자동으로 생성합니다.
@RequestMapping("/api/matchings")
@RestController
public class MatchingController {

    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
        log.info("MatchingController initialized");
    }

    // 모든 매칭 가져오기
    @GetMapping("/admin")
    public ResponseEntity<?> getAllMatching() {
        try {
            log.info("Fetching all matchings");
            return ResponseEntity.ok(matchingService.getAllMatching());
        } catch (Exception e) {
            log.error("Error fetching all matchings: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Error fetching all matchings."));
        }
    }

    @GetMapping("/admin/matchings/{matching-id}")
    public ResponseEntity<?> getMatching(@PathVariable(name = "matching-id") Long matchingId) {
        try {
            log.info("Fetching matching with ID: {}", matchingId);
            return ResponseEntity.ok(matchingService.getMatching(matchingId));
        } catch (Exception e) {
            log.error("Error fetching matching with ID {}: {}", matchingId, e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Error fetching matching."));
        }
    }

    @PatchMapping("/admin/matchings/{matching-id}")
    public ResponseEntity<?> updateMatching(@PathVariable(name = "matching-id") Long matchingId, @RequestBody MatchingStatusDto matchingStatusDTO) {
        try {
            log.info("Updating matching with ID: {}", matchingId);
            MatchingStatus matchingStatus = matchingStatusDTO.getMatchingStatus();
            if (matchingService.updateMatching(matchingId, matchingStatus)) {
                log.info("Matching with ID: {} updated successfully", matchingId);
                return ResponseEntity.ok(Map.of("message", "Matching successfully updated."));
            } else {
                log.error("Failed to update matching with ID: {}", matchingId);
                return ResponseEntity.badRequest().body(Map.of("message", "Failed to update matching."));
            }
        } catch (Exception e) {
            log.error("Error updating matching with ID {}: {}", matchingId, e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("message", "Error updating matching."));
        }
    }

    @PostMapping("/project/{project-item-id}/request")
    public ResponseEntity<?> addMatchingByProjectManager(@PathVariable(name = "project-item-id") Long projectItemId, @RequestBody Map<String, Long> spaceItemIdInfo) {
        try {
            log.info("Adding matching by project manager for projectItemId: {}, with spaceItemId: {}", projectItemId, spaceItemIdInfo.get("spaceItemId"));

            Long spaceItemId = spaceItemIdInfo.get("spaceItemId");

            if (matchingService.addMatchingByProjectManager(projectItemId, spaceItemId) != null) {
                log.info("Matching added successfully for projectItemId: {}", projectItemId);
                return ResponseEntity.ok(Map.of("message", "Matching successfully added for projectItemId " + projectItemId));
            } else {
                log.error("Matching addition failed for projectItemId: {}", projectItemId);
                return ResponseEntity.badRequest().body(Map.of("message", "Matching addition failed for unknown reasons for projectItemId " + projectItemId));
            }
        } catch (Exception e) {
            log.error("An exception occurred while adding matching for projectItemId: {}", projectItemId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "An error occurred while processing your request."));
        }
    }

    @PostMapping("/space/{space-item-id}/request")
    public ResponseEntity<?> addMatchingBySpaceRental(@PathVariable(name = "space-item-id") Long spaceItemId, @RequestBody Map<String, Long> projectItemIdInfo) {
        try {
            log.info("Adding matching by space rental for space item ID: {}", spaceItemId);
            Long projectItemId = projectItemIdInfo.get("projectItemId");

            if (matchingService.addMatchingBySpaceRental(spaceItemId, projectItemId) != null) {
                log.info("Matching added successfully for space item ID: {}", spaceItemId);
                return ResponseEntity.ok(Map.of("message", "Matching successfully added."));
            } else {
                log.error("Failed to add matching for space item ID: {}", spaceItemId);
                return ResponseEntity.badRequest().body(Map.of("message", "Failed to add matching."));
            }
        } catch (Exception e) {
            log.error("An exception occurred while adding matching for space item ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "An error occurred while processing your request."));
        }
    }

    @PostMapping("/{matching-id}")
    public ResponseEntity<?> approvalMatching(@PathVariable(name = "matching-id") Long id, @RequestBody boolean accept) {
        try {
            log.info("Approval status update for matchingId: {}, accept: {}", id, accept);

            if (matchingService.updateMatching(id, accept ? MatchingStatus.WAITING : MatchingStatus.CANCEL)) {
                log.info("Matching status updated successfully for matchingId: {}", id);
                return ResponseEntity.ok(Map.of("message", "Matching status successfully updated."));
            }
            log.error("Failed to update matching status for matchingId: {}", id);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to update matching status."));
        } catch (Exception e) {
            log.error("An exception occurred while updating matching status for matchingId: {}", id, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "An error occurred while processing your request."));
        }
    }


    @GetMapping("/space-rental/{space-rental-id}/waitings")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalWaitingMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        log.info("Fetching waiting matchings for spaceRentalId: {}", spaceRentalId);
        return ResponseEntity.ok(matchingService.findSpaceRentalWaitingMatching(spaceRentalId));
    }

    @GetMapping("/space-rental/{space-rental-id}/offers")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalOfferMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        log.info("Fetching offer matchings for spaceRentalId: {}", spaceRentalId);
        return ResponseEntity.ok(matchingService.findSpaceRentalOfferMatching(spaceRentalId));
    }

    @GetMapping("/space-rental/{space-rental-id}/success")
    public ResponseEntity<List<MatchingResponseDto>> findSpaceRentalSuccessMatching(@PathVariable(name = "space-rental-id") Long spaceRentalId) {
        log.info("Fetching successful matchings for spaceRentalId: {}", spaceRentalId);
        return ResponseEntity.ok(matchingService.findSpaceRentalSuccessMatching(spaceRentalId));
    }

    @GetMapping("/project-managers/{project-manager-id}/waitings")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerWaitingMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        log.info("Fetching waiting matchings for projectManagerId: {}", projectManagerId);
        return ResponseEntity.ok(matchingService.findProjectManagerWaitingMatching(projectManagerId));
    }

    @GetMapping("/project-managers/{project-manager-id}/offers")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerOfferMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        log.info("Fetching offer matchings for projectManagerId: {}", projectManagerId);
        return ResponseEntity.ok(matchingService.findProjectManagerOfferMatching(projectManagerId));
    }

    @GetMapping("/project-managers/{project-manager-id}/success")
    public ResponseEntity<List<MatchingResponseDto>> findProjectManagerSuccessMatching(@PathVariable(name = "project-manager-id") Long projectManagerId) {
        log.info("Fetching successful matchings for projectManagerId: {}", projectManagerId);
        return ResponseEntity.ok(matchingService.findProjectManagerSuccessMatching(projectManagerId));
    }
}
