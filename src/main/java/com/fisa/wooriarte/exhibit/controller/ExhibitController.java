package com.fisa.wooriarte.exhibit.controller;

import com.fisa.wooriarte.exhibit.dto.ExhibitDto;
import com.fisa.wooriarte.exhibit.service.ExhibitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
public class ExhibitController {

    private final ExhibitService exhibitService;

    @Autowired
    public ExhibitController(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }

    //모든 전시 출력
    @GetMapping({"/admin/exhibits", "/exhibits"})
    public ResponseEntity<List<ExhibitDto>> findAllExhibits() {
        List<ExhibitDto> exhibits = exhibitService.findAllExhibits();
        return ResponseEntity.ok(exhibits);
    }

    //하나의 전시 출력
    @GetMapping({"/admin/exhibits/{exhibit-id}", "/exhibits/{exhibit-id}"})
    public ResponseEntity<ExhibitDto> findExhibitById(@PathVariable(name = "exhibit-id") Long exhibitId) {
        Optional<ExhibitDto> exhibitOptional = exhibitService.findExhibitbyId(exhibitId);

        return exhibitOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //전시 정보 생성 (매칭 성공)
    @PostMapping("/admin/matchings/{matching-id}/exhibits")
    public ResponseEntity<String> addExhibit(@PathVariable(name = "matching-id") Long matchingId, @RequestBody ExhibitDto exhibitDTO) {
        try {
            exhibitService.addExhibit(exhibitDTO, matchingId);
            return ResponseEntity.status(HttpStatus.CREATED).body("전시 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 생성 중 오류 발생: " + e.getMessage());
        }
    }

    //전시 수정
    @PutMapping("/admin/exhibits/{exhibit-id}")
    public ResponseEntity<String> updateExhibit(@PathVariable(name = "exhibit-id") Long exhibitId, @RequestBody ExhibitDto exhibitDTO) {
        try {
            exhibitService.updateExhibit(exhibitId, exhibitDTO);
            return ResponseEntity.ok("전시 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 수정 실패: " + e.getMessage());
        }
    }


    @DeleteMapping("/exhibit/{exhibit-id}")
    public ResponseEntity<String> deleteExhibit(@PathVariable(name = "exhibit-id") Long exhibitId) {
        try {
            exhibitService.deleteExhibitById(exhibitId);
            return ResponseEntity.ok("전시가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("전시를 삭제하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
