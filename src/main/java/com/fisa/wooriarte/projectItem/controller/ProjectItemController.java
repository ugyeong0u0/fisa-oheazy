package com.fisa.wooriarte.projectItem.controller;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.dto.ProjectItemResponseDto;
import com.fisa.wooriarte.projectItem.service.ProjectItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/api/project-items")
@RestController
public class ProjectItemController {

    private static final Logger log = LoggerFactory.getLogger(ProjectItemController.class);

    private final ProjectItemService projectItemService;

    @Autowired
    public ProjectItemController(ProjectItemService projectItemService) {
        this.projectItemService = projectItemService;
    }

    // 승인된 모든 프로젝트 아이템 정보를 조회
    @GetMapping("/approved-all")
    public ResponseEntity<List<ProjectItemResponseDto>> getAllApprovedProjectItemInfo() {
        try {
            List<ProjectItemResponseDto> items = projectItemService.findApprovedAll();
            return ResponseEntity.ok(items);
        } catch (NoSuchElementException e) {
            log.error("승인된 프로젝트 아이템 조회 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("모든 프로젝트 아이템 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 승인되지 않은 모든 프로젝트 아이템 정보를 조회
    @GetMapping("/unapproved-all")
    public ResponseEntity<List<ProjectItemResponseDto>> getAllUnapprovedProjectItemInfo() {
        try {
            List<ProjectItemResponseDto> items = projectItemService.findUnapprovedAll();
            return ResponseEntity.ok(items);
        } catch (NoSuchElementException e) {
            log.error("승인되지 않은 프로젝트 아이템 조회 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("모든 프로젝트 아이템 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 특정 프로젝트 관리자 ID의 프로젝트 아이템 조회
    @GetMapping("/project-manager/{project-manager-id}")
    public ResponseEntity<List<ProjectItemResponseDto>> getProjectItemByProjectManagerId(@PathVariable("project-manager-id") Long projectManagerId) {
        try {
            List<ProjectItemResponseDto> projectItemDtoList = projectItemService.findByProjectManagerId(projectManagerId);
            return ResponseEntity.ok(projectItemDtoList);
        } catch (NoSuchElementException e) {
            log.error("해당 프로젝트 관리자 ID에 속한 프로젝트 아이템을 찾을 수 없음 - ID: {}", projectManagerId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("프로젝트 아이템 조회 중 오류 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 아이템 추가
    @PostMapping("")
    public ResponseEntity<String> addProjectItem(@RequestBody ProjectItemDto projectItemDTO) {
        try {
            Long projectItemId = projectItemService.addProjectItem(projectItemDTO);
            return ResponseEntity.ok(projectItemId.toString());
        } catch (NoSuchElementException e) {
            log.error("프로젝트 관리자 정보가 없음", e);
            return ResponseEntity.badRequest().body("프로젝트 관리자 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("프로젝트 아이템 추가 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("프로젝트 아이템 추가 중 오류가 발생했습니다.");
        }
    }

    // 특정 프로젝트 아이템의 세부 정보 조회
    @GetMapping("/{project-item-id}")
    public ResponseEntity<ProjectItemDto> getProjectItemInfo(@PathVariable(name = "project-item-id") Long projectItemId) {
        try {
            ProjectItemDto projectItemDto = projectItemService.findByProjectItemId(projectItemId);
            return ResponseEntity.ok(projectItemDto);
        } catch (NoSuchElementException e) {
            log.error("해당 프로젝트 아이템을 찾을 수 없음 - ID: {}", projectItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("프로젝트 아이템 조회 중 오류 발생 - ID: {}", projectItemId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 아이템 정보 수정
    @PutMapping("/{project-item-id}")
    public ResponseEntity<String> updateProjectItem(@PathVariable(name = "project-item-id") Long projectItemId, @RequestBody ProjectItemDto projectItemDTO) {
        try {
            projectItemService.updateProjectItem(projectItemId, projectItemDTO);
            return ResponseEntity.ok("프로젝트 아이템 수정 성공");
        } catch (NoSuchElementException e) {
            log.error("해당 프로젝트 아이템을 찾을 수 없음 - ID: {}", projectItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로젝트 아이템을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("프로젝트 아이템 수정 중 오류 발생 - ID: {}", projectItemId, e);
            return ResponseEntity.internalServerError().body("프로젝트 아이템 수정 중 오류가 발생했습니다.");
        }
    }

    // 프로젝트 아이템 삭제
    @DeleteMapping("/{project-item-id}")
    public ResponseEntity<String> deleteProjectItem(@PathVariable(name = "project-item-id") Long projectItemId) {
        try {
            projectItemService.deleteProjectItem(projectItemId);
            return ResponseEntity.ok("프로젝트 아이템 삭제 성공");
        } catch (NoSuchElementException e) {
            log.error("해당 프로젝트 아이템을 찾을 수 없음 - ID: {}", projectItemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로젝트 아이템을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("프로젝트 아이템 삭제 중 오류 발생 - ID: {}", projectItemId, e);
            return ResponseEntity.internalServerError().body("프로젝트 아이템 삭제 중 오류가 발생했습니다.");
        }
    }

    // 필터 조건에 따라 프로젝트 아이템 조회
    @GetMapping("/{start-date}/{end-date}/{city}")
    public ResponseEntity<List<ProjectItemResponseDto>> getProjectItemByFilter(@PathVariable("start-date") LocalDate startDate, @PathVariable("end-date") LocalDate endDate, @PathVariable("city") String city) {
        try {
            List<ProjectItemResponseDto> projectItemDtoList = projectItemService.findByFilter(startDate, endDate, city);
            return ResponseEntity.ok(projectItemDtoList);
        } catch (Exception e) {
            log.error("필터 조건에 따른 프로젝트 아이템 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}