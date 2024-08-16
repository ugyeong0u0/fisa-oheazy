package com.fisa.wooriarte.projectmanager.controller;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.projectmanager.dto.ProjectManagerDto;
import com.fisa.wooriarte.projectmanager.service.ProjectManagerService;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RequestMapping("/api/project-managers")
@RestController
@Slf4j
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    @Autowired
    public ProjectManagerController(ProjectManagerService projectManagerService) {
        this.projectManagerService = projectManagerService;
    }

    // 프로젝트 매니저 추가
    @PostMapping("")
    public ResponseEntity<String> addProjectManager(@RequestBody ProjectManagerDto projectManagerDTO) {
        try {
            log.info("프로젝트 매니저 추가 시도");
            projectManagerService.addProjectManager(projectManagerDTO);
            return ResponseEntity.ok("프로젝트 매니저 추가 성공");
        } catch (Exception e) {
            log.error("프로젝트 매니저 추가 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().body("프로젝트 매니저 추가 중 예외 발생");
        }
    }

    // 프로젝트 매니저 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginProjectManager(@RequestBody Map<String, String> loginInfo) {
        try {
            log.info("프로젝트 매니저 로그인 시도");
            String id = loginInfo.get("id");
            String pwd = loginInfo.get("pwd");
            ProjectManagerDto projectManagerDto = projectManagerService.loginProjectManager(id, pwd);
            return ResponseEntity.ok(projectManagerDto.getProjectManagerId().toString());
        } catch (NoSuchElementException e) {
            log.error("프로젝트 매니저 로그인 실패 - 사용자 없음: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패 - 사용자 없음");
        } catch (Exception e) {
            log.error("프로젝트 매니저 로그인 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().body("로그인 중 예외 발생");
        }
    }

    // JWT 로그인
    @PostMapping("/jwtlogin")
    public ResponseEntity<JwtToken> login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        try {
            log.info("JWT로 프로젝트 매니저 로그인 시도");
            JwtToken token = projectManagerService.login(userLoginRequestDTO.getId(), userLoginRequestDTO.getPwd());
            return ResponseEntity.ok(token);
        } catch (NoSuchElementException e) {
            log.error("JWT 로그인 실패 - 사용자 없음: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("JWT 로그인 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 매니저 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<String> findProjectManagerId(@RequestBody String email) {
        try {
            log.info("프로젝트 매니저 ID 찾기 시도");
            String id = projectManagerService.getId(email);
            return ResponseEntity.ok(id);
        } catch (NoSuchElementException e) {
            log.error("프로젝트 매니저 ID 찾기 실패 - 사용자 없음: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로젝트 매니저 ID를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("프로젝트 매니저 ID 찾기 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().body("ID 찾기 중 예외 발생");
        }
    }

    // 프로젝트 매니저 비밀번호 설정
    @PostMapping("/set-pwd")
    public ResponseEntity<String> setProjectManagerPw(@RequestBody Map<String, String> pwdInfo) {
        try {
            log.info("프로젝트 매니저 비밀번호 재설정 시도");
            String id = pwdInfo.get("id");
            String newPwd = pwdInfo.get("new_pwd");
            boolean success = projectManagerService.setPwd(id, newPwd);
            return success ? ResponseEntity.ok("비밀번호 재설정 성공") : ResponseEntity.badRequest().body("비밀번호 재설정 실패");
        } catch (NoSuchElementException e) {
            log.error("비밀번호 재설정 실패 - 사용자 없음: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("비밀번호 재설정 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().body("비밀번호 재설정 중 예외 발생");
        }
    }

    @GetMapping("/{project-manager-id}")
    public ResponseEntity<ProjectManagerDto> getProjectManagerInfo(@PathVariable("project-manager-id") Long projectManagerId) {
        log.info("프로젝트 매니저 정보 조회 시도 - ID: {}", projectManagerId);
        try {
            return projectManagerService.findByProjectManagerId(projectManagerId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 바뀐 부분
        } catch (Exception e) {
            log.error("프로젝트 매니저 정보 조회 중 예외 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 프로젝트 매니저 정보 업데이트
    @PutMapping("/{project-manager-id}")
    public ResponseEntity<String> updateProjectManagerInfo(
            @PathVariable("project-manager-id") Long projectManagerId,
            @RequestBody ProjectManagerDto projectManagerDTO) {
        log.info("프로젝트 매니저 정보 업데이트 시도 - ID: {}", projectManagerId);
        try {
            boolean updated = projectManagerService.updateProjectManager(projectManagerId, projectManagerDTO);
            return updated ? ResponseEntity.ok("프로젝트 매니저 정보 업데이트 성공") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로젝트 매니저를 찾을 수 없음");
        } catch (Exception e) {
            log.error("프로젝트 매니저 정보 업데이트 중 예외 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body("정보 업데이트 중 예외 발생");
        }
    }

    // 프로젝트 매니저 삭제
    @DeleteMapping("/{project-manager-id}")
    public ResponseEntity<String> deleteProjectManager(@PathVariable("project-manager-id") Long projectManagerId) {
        log.info("프로젝트 매니저 삭제 시도 - ID: {}", projectManagerId);
        try {
            boolean deleted = projectManagerService.deleteProjectManager(projectManagerId);
            return deleted ? ResponseEntity.ok("프로젝트 매니저 삭제 성공") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로젝트 매니저를 찾을 수 없음");
        } catch (Exception e) {
            log.error("프로젝트 매니저 삭제 중 예외 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body("삭제 중 예외 발생");
        }
    }

    // 프로젝트 매니저 비밀번호 확인
    @PostMapping("/{project-manager-id}/verify-pwd")
    public ResponseEntity<String> verifyProjectManagerPassword(@PathVariable("project-manager-id") Long projectManagerId, @RequestBody ProjectManagerDto projectManagerDto) {
        try {
            log.info("프로젝트 매니저 비밀번호 확인 시도 - ID: {}", projectManagerId);
            boolean success = projectManagerService.verifyPassword(projectManagerId, projectManagerDto.getPwd());
            return success ? ResponseEntity.ok("비밀번호 확인 성공") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 확인 실패");
        } catch (NoSuchElementException e) {
            log.error("비밀번호 확인 실패 - 사용자 없음: {}", projectManagerId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("비밀번호 확인 중 예외 발생 - ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body("비밀번호 확인 중 예외 발생");
        }
    }
}