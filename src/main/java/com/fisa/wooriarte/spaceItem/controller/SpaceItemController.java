package com.fisa.wooriarte.spaceItem.controller;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemResponseDto;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/api/space-items")
@RestController
public class SpaceItemController {

    private static final Logger log = LoggerFactory.getLogger(SpaceItemController.class);

    private final SpaceItemService spaceItemService;

    @Autowired
    public SpaceItemController(SpaceItemService spaceItemService) {
        this.spaceItemService = spaceItemService;
    }

    // 승인된 모든 공간 아이템 정보를 조회
    @GetMapping("/approved-all")
    public ResponseEntity<List<SpaceItemResponseDto>> getAllApprovedSpaceItemInfo() {
        try {
            List<SpaceItemResponseDto> items = spaceItemService.findApprovedAll();
            return ResponseEntity.ok(items);
        } catch (NoSuchElementException e) {
            log.error("승인된 공간 아이템 조회 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("모든 공간 아이템 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 승인되지 않은 모든 공간 아이템 정보 조회
    @GetMapping("/unapproved-all")
    public ResponseEntity<List<SpaceItemResponseDto>> getAllUnapprovedSpaceItemInfo() {
        try {
            List<SpaceItemResponseDto> items = spaceItemService.findUnapprovedAll();
            return ResponseEntity.ok(items);
        } catch (NoSuchElementException e) {
            log.error("승인되지 않은 공간 아이템 조회 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("승인되지 않은 모든 공간 아이템 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 특정 임대사업자 ID의 공간 아이템 조회
    @GetMapping("/space-rental/{space-rental-id}")
    public ResponseEntity<List<SpaceItemResponseDto>> getSpaceItemBySpaceRentalId(@PathVariable("space-rental-id") Long spaceRentalId) {
        try {
            List<SpaceItemResponseDto> spaceItemDtoList = spaceItemService.findBySpaceRentalId(spaceRentalId);
            return ResponseEntity.ok(spaceItemDtoList);
        } catch (NoSuchElementException e) {
            log.error("해당 임대사업자 ID에 속한 공간 아이템을 찾을 수 없음 - ID: {}", spaceRentalId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("공간 아이템 조회 중 오류 발생 - ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // 공간 아이템 추가
    @PostMapping("")
    public ResponseEntity<String> addSpaceItem(@RequestBody SpaceItemDto spaceItemDTO) {
        try {
            Long spaceItemId = spaceItemService.addSpaceItem(spaceItemDTO);
            return ResponseEntity.ok(spaceItemId.toString());
        } catch (NoSuchElementException e) {
            log.error("임대사업자 정보가 없음", e);
            return ResponseEntity.badRequest().body("임대사업자 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("공간 아이템 추가 중 오류 발생", e);
            return ResponseEntity.badRequest().body("공간 아이템 추가 중 오류가 발생했습니다.");
        }
    }

    // 특정 공간 아이템의 세부 정보 조회
    @GetMapping("/{space-item-id}")
    public ResponseEntity<SpaceItemDto> getSpaceItemInfo(@PathVariable(name = "space-item-id") Long spaceItemId) {
        try {
            SpaceItemDto spaceItemDto = spaceItemService.findSpaceItemById(spaceItemId);
            return ResponseEntity.ok(spaceItemDto);
        } catch (NoSuchElementException e) {
            log.error("해당 공간 아이템을 찾을 수 없음 - ID: {}", spaceItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("공간 아이템 조회 중 오류 발생 - ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 공간 아이템 정보 수정
    @PutMapping("/{space-item-id}")
    public ResponseEntity<String> updateSpaceItem(@PathVariable(name = "space-item-id") Long spaceItemId, @RequestBody SpaceItemDto spaceItemDTO) {
        try {
            spaceItemService.updateSpaceItem(spaceItemId, spaceItemDTO);
            return ResponseEntity.ok("공간 아이템 수정 성공");
        } catch (NoSuchElementException e) {
            log.error("해당 공간 아이템을 찾을 수 없음 - ID: {}", spaceItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("공간 아이템을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("공간 아이템 수정 중 오류 발생 - ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().body("공간 아이템 수정 중 오류가 발생했습니다.");
        }
    }

    // 공간 아이템 삭제
    @DeleteMapping("/{space-item-id}")
    public ResponseEntity<String> deleteSpaceItem(@PathVariable(name = "space-item-id") Long spaceItemId) {
        try {
            spaceItemService.deleteSpaceItem(spaceItemId);
            return ResponseEntity.ok("공간 아이템 삭제 성공");
        } catch (NoSuchElementException e) {
            log.error("해당 공간 아이템을 찾을 수 없음 - ID: {}", spaceItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("공간 아이템을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("공간 아이템 삭제 중 오류 발생 - ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().body("공간 아이템 삭제 중 오류가 발생했습니다.");
        }
    }

    // 필터 조건에 따라 공간 아이템 조회
    @GetMapping("/{start-date}/{end-date}/{city}")
    public ResponseEntity<List<SpaceItemResponseDto>> getSpaceItemByFilter(@PathVariable("start-date") LocalDate startDate, @PathVariable("end-date") LocalDate endDate, @PathVariable("city") String city) {
        try {
            List<SpaceItemResponseDto> spaceItemDtoList = spaceItemService.findByFilter(startDate, endDate, city);
            return ResponseEntity.ok(spaceItemDtoList);
        } catch (Exception e) {
            log.error("필터 조건에 따른 공간 아이템 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}