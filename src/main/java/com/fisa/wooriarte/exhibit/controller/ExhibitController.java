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

    /**
     * 1. 진행 중인 모든 전시 출력
     * @return
     */
    @GetMapping({"/admin/exhibits", "/exhibits"})
    public ResponseEntity<?> findAllExhibits() {
        try {
            List<ExhibitDto> exhibits = exhibitService.findAllExhibits();
            if (exhibits.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("전시가 존재하지 않습니다.");
            }
            return ResponseEntity.ok(exhibits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping({"/admin/exhibits/{exhibit-id}", "/exhibits/{exhibit-id}"})
    public ResponseEntity<ExhibitDto> findExhibitById(@PathVariable(name = "exhibit-id") Long exhibitId) {
        Optional<ExhibitDto> exhibitOptional = exhibitService.findExhibitById(exhibitId);

        if (exhibitOptional.isPresent()) {
            return ResponseEntity.ok(exhibitOptional.get());
        } else {
            // 실패 시 적절한 타입을 유지하면서 오류 메시지를 전달하는 방법이 필요함
            // 여기서는 ResponseEntity<?>를 사용하지 않고, 단순히 상태 코드만 반환하도록 선택
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    /**
     * 3. 전시 생성 (매칭 성공시)
     * @param matchingId : 생성할 전시의 matchingId
     * @param exhibitDto : 생성할 전시 데이터
     * @return
     */
    @PostMapping("/admin/matchings/{matching-id}/exhibits")
    public ResponseEntity<String> addExhibit(@PathVariable(name = "matching-id") Long matchingId, @RequestBody ExhibitDto exhibitDto) {
        try {
            exhibitService.addExhibit(exhibitDto, matchingId);
            return ResponseEntity.status(HttpStatus.CREATED).body("전시 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 생성 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 4. 전시 수정
     * @param exhibitId : 수정할 exhibitId
     * @param exhibitDto : 수정에 대입될 전시 정보 (name, intro, startDate, endDate, price, city)
     * @return
     */
    @PutMapping("/admin/exhibits/{exhibit-id}")
    public ResponseEntity<String> updateExhibit(@PathVariable(name = "exhibit-id") Long exhibitId, @RequestBody ExhibitDto exhibitDto) {
        try {
            exhibitService.updateExhibit(exhibitId, exhibitDto);
            return ResponseEntity.ok("전시 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 수정 실패: " + e.getMessage());
        }
    }

    /**
     * 5. 전시 삭제
     * @param exhibitId : 삭제할 exhibitId
     * @return
     */
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
