package com.fisa.wooriarte.admin.controller;

import com.fisa.wooriarte.admin.dto.AdminDto;
import com.fisa.wooriarte.admin.service.AdminService;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.service.ProjectItemService;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final ProjectItemService projectItemService;
    private final SpaceItemService spaceItemService;

    @Autowired
    public AdminController(AdminService adminService, ProjectItemService projectItemService, SpaceItemService spaceItemService) {
        this.adminService = adminService;
        this.projectItemService = projectItemService;
        this.spaceItemService = spaceItemService;
    }

    // 관리자 추가
    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDto adminDto) {
        try {
            log.info("관리자 추가 시도");
            adminService.addAdmin(adminDto);
            return ResponseEntity.ok("관리자 추가 성공");
        } catch (Exception e) {
            log.error("관리자 추가 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().body("관리자 추가 실패");
        }
    }

    // JWT를 이용한 관리자 로그인
    @PostMapping("/jwtlogin")
    public ResponseEntity<JwtToken> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            log.info("JWT를 이용한 관리자 로그인 시도");
            JwtToken token = adminService.login(userLoginRequestDto.getId(), userLoginRequestDto.getPwd());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("JWT 로그인 중 예외 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 승인 대기 중인 아이템 관리
    @GetMapping("/manage-item-approval")
    public ResponseEntity<Map<String, List<?>>> manageItemApproval() {
        try {
            log.info("아이템 승인 관리 시도");
            List<ProjectItemDto> projectItemList = projectItemService.getUnapprovedItems();
            List<SpaceItemDto> spaceItemList = spaceItemService.getUnapprovedItems();
            return ResponseEntity.ok(Map.of("projectItems", projectItemList, "spaceItems", spaceItemList));
        } catch (Exception e) {
            log.error("아이템 승인 관리 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 아이템 승인
    @PostMapping("/approve-project-item/{project-item-id}")
    public ResponseEntity<String> projectItemApproval(@PathVariable("project-item-id") Long projectItemId) {
        try {
            log.info("프로젝트 아이템 승인 시도 - ID: {}", projectItemId);
            projectItemService.approveItem(projectItemId);
            return ResponseEntity.ok("프로젝트 아이템 승인 성공");
        } catch (Exception e) {
            log.error("프로젝트 아이템 승인 중 예외 발생 - ID: {}", projectItemId, e);
            return ResponseEntity.internalServerError().body("프로젝트 아이템 승인 실패");
        }
    }

    // 공간 아이템 승인
    @PostMapping("/approve-space-item/{space-item-id}")
    public ResponseEntity<String> spaceItemApproval(@PathVariable("space-item-id") Long spaceItemId) {
        try {
            log.info("공간 아이템 승인 시도 - ID: {}", spaceItemId);
            spaceItemService.approveItem(spaceItemId);
            return ResponseEntity.ok("공간 아이템 승인 성공");
        } catch (Exception e) {
            log.error("공간 아이템 승인 중 예외 발생 - ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().body("공간 아이템 승인 실패");
        }
    }

    // 프로젝트 아이템 거절
    @PostMapping("/refuse-project-item/{project-item-id}")
    public ResponseEntity<String> refuseProjectItem(@PathVariable("project-item-id") Long projectItemId) {
        try {
            log.info("프로젝트 아이템 거절 시도 - ID: {}", projectItemId);
            projectItemService.refuseItem(projectItemId);
            return ResponseEntity.ok("프로젝트 아이템 거절 성공");
        } catch (Exception e) {
            log.error("프로젝트 아이템 거절 중 예외 발생 - ID: {}", projectItemId, e);
            return ResponseEntity.internalServerError().body("프로젝트 아이템 거절 실패");
        }
    }

    // 공간 아이템 거절
    @PostMapping("/refuse-space-item/{space-item-id}")
    public ResponseEntity<String> refuseSpaceItem(@PathVariable("space-item-id") Long spaceItemId) {
        try {
            log.info("공간 아이템 거절 시도 - ID: {}", spaceItemId);
            spaceItemService.refuseItem(spaceItemId);
            return ResponseEntity.ok("공간 아이템 거절 성공");
        } catch (Exception e) {
            log.error("공간 아이템 거절 중 예외 발생 - ID: {}", spaceItemId, e);
            return ResponseEntity.internalServerError().body("공간 아이템 거절 실패");
        }
    }

    // 헬스 체크 엔드포인트
    @GetMapping("/health-check")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}