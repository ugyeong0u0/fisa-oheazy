package com.fisa.wooriarte.projectmanager.controller;

import com.fisa.wooriarte.projectmanager.DTO.ProjectManagerDTO;
import com.fisa.wooriarte.projectmanager.service.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/project-manager")
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    @Autowired
    public ProjectManagerController(ProjectManagerService projectManagerService) {
        this.projectManagerService = projectManagerService;
    }

    //프로젝트 매니저 회원가입
    @PostMapping("")
    public ResponseEntity<String> addProjectManager(@RequestBody ProjectManagerDTO projectManagerDTO) {
        boolean added = projectManagerService.addProjectManager(projectManagerDTO);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Project manager added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add project manager.");
        }
    }

    //프로젝트 매니저 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginProjectManager(@RequestBody Map<String, String> loginInfo) {
        String id = loginInfo.get("id");
        String pwd = loginInfo.get("pwd");
        boolean loggedIn = projectManagerService.loginProjectManager(id, pwd);
        if (loggedIn) {
            return ResponseEntity.ok("Login success.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed. Invalid credentials.");
        }
    }

    //공간대여자 아이디 찾기
    @PostMapping("/find-id")
    public String findBusinessId(@RequestBody String email) {
        return projectManagerService.getId(email);
    }

    // 공간대여자 비밀번호 재설정
    @PostMapping("/set-pw")
    public String findBusinessPass(@RequestBody Map<String, String> pwdInfo) {
        Long projectManagerId = Long.parseLong(pwdInfo.get("project_manager_id"));
        String newPwd = pwdInfo.get("new_pwd");
        if(projectManagerService.setPwd(projectManagerId, newPwd))
            return "success";
        return "fail";
    }

    //프로젝트 매니저 정보 조회
    @GetMapping("/{project-manager-id}")
    public ResponseEntity<ProjectManagerDTO> getProjectManagerInfo(@PathVariable("project-manager-id") Long projectManagerId) {
        Optional<ProjectManagerDTO> projectManageroptional = projectManagerService.findByProjectManagerId(projectManagerId);
        return projectManageroptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //프로젝트 매니저 정보 갱신
    @PutMapping("{project-manager-id}")
    public ResponseEntity<String> updateProjectManagerInfo(
            @PathVariable("project-manager-id") Long projectManagerId,
            @RequestBody ProjectManagerDTO projectManagerDTO) {

        boolean updated = projectManagerService.updateProjectManager(projectManagerId, projectManagerDTO);
        if (updated) {
            return ResponseEntity.ok("Project manager information updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update project manager information. Project manager not found.");
        }
    }

    //프로젝트 매니저 삭제(delete를 true로 변경)
    public ResponseEntity<String> deleteProjectManager(@PathVariable("project-manager-id") Long projectManagerId) {
        boolean deleted = projectManagerService.deleteProjectManager(projectManagerId);
        if (deleted) {
            return ResponseEntity.ok("Project manager deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete project manager. Project manager not found.");
        }
    }

    /*
    예외 처리하는 부분
    DataIntegrityViolationException: 데이터베이스 제약조건, 무결성 위반 시도 (이미 존재하는 아이디 삽입, 이미 삭제된 사용자 삭제 등등)
    NoSuchElementException: 검색 요소가 발견되지 않음 (해당 아이디 찾을 수 없음 등등)
  */
    @ExceptionHandler({NoSuchElementException.class})
    public String handleNoSuchElementException(Exception e) {
        return "해당 id를 사용하는 유저가 없습니다";
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public String handleDataIntegrityViolationException(Exception e) {
        return "불가능한 요청입니다";
    }
}
