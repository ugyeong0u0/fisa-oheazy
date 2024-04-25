package com.fisa.wooriarte.projectmanager.controller;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.projectmanager.dto.ProjectManagerDto;
import com.fisa.wooriarte.projectmanager.service.ProjectManagerService;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.NoSuchElementException;

@RequestMapping("/api/project-managers")
@RestController
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;
    private final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);

    @Autowired
    public ProjectManagerController(ProjectManagerService projectManagerService) {
        this.projectManagerService = projectManagerService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> addProjectManager(@RequestBody ProjectManagerDto projectManagerDTO) {
        try {
            log.info("Trying to add project manager");
            boolean added = projectManagerService.addProjectManager(projectManagerDTO);
            if (added) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Project manager added successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to add project manager."));
            }
        } catch (Exception e) {
            log.error("Exception occurred while adding project manager: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to add project manager due to an exception."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginProjectManager(@RequestBody Map<String, String> loginInfo) {
        try {
            log.info("Trying to login project manager");
            String id = loginInfo.get("id");
            String pwd = loginInfo.get("pwd");
            ProjectManagerDto projectManagerDto = projectManagerService.loginProjectManager(id, pwd);
            return ResponseEntity.status(HttpStatus.OK).body(projectManagerDto.getProjectManagerId());

        } catch (Exception e) {
            log.error("Exception occurred while logging in project manager: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Login failed due to an exception.");
        }

    }

    @PostMapping("/jwtlogin")
    public JwtToken login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        try {
            log.info("Trying to login with JWT");
            String id = userLoginRequestDTO.getId();
            String pwd = userLoginRequestDTO.getPwd();
            return projectManagerService.login(id, pwd);
        } catch (Exception e) {
            log.error("Exception occurred while logging in with JWT: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed due to an exception.");
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<Map<String, String>> findProjectManagerId(@RequestBody String email) {
        try {
            log.info("Trying to find business ID");
            String id = projectManagerService.getId(email);
            return ResponseEntity.ok(Map.of("message", id));
        } catch (Exception e) {
            log.error("Exception occurred while finding business ID: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to find business ID due to an exception."));
        }
    }

    @PostMapping("/set-pwd")
    public ResponseEntity<Map<String, String>> setProjectManagerPw(@RequestBody Map<String, String> pwdInfo) {
        try {
            log.info("Trying to reset password");
            String id = pwdInfo.get("id");
            String newPwd = pwdInfo.get("new_pwd");
            boolean success = projectManagerService.setPwd(id, newPwd);
            if (success)
                return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
            else
                return ResponseEntity.badRequest().body(Map.of("message", "Password reset failed."));
        } catch (Exception e) {
            log.error("Exception occurred while resetting password: ", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Password reset failed due to an exception."));
        }
    }

    @GetMapping("/{project-manager-id}")
    public ResponseEntity<?> getProjectManagerInfo(@PathVariable("project-manager-id") Long projectManagerId) {
        log.info("Attempting to find project manager with ID: {}", projectManagerId);
        try {
            return projectManagerService.findByProjectManagerId(projectManagerId)
                    .map(projectManager -> ResponseEntity.ok(projectManager))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error finding project manager with ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error."));
        }
    }

    // 프로젝트 매니저 정보 업데이트
    @PutMapping("/{project-manager-id}")
    public ResponseEntity<Map<String, String>> updateProjectManagerInfo(
            @PathVariable("project-manager-id") Long projectManagerId,
            @RequestBody ProjectManagerDto projectManagerDTO) {
        log.info("Attempting to update project manager with ID: {}", projectManagerId);
        try {
            boolean updated = projectManagerService.updateProjectManager(projectManagerId, projectManagerDTO);
            if (updated) {
                return ResponseEntity.ok(Map.of("message", "Project manager information updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Failed to update project manager information. Project manager not found."));
            }
        } catch (Exception e) {
            log.error("Error updating project manager with ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error."));
        }
    }

    // 프로젝트 매니저 삭제
    @DeleteMapping("/{project-manager-id}")
    public ResponseEntity<Map<String, String>> deleteProjectManager(@PathVariable("project-manager-id") Long projectManagerId) {
        log.info("Attempting to delete project manager with ID: {}", projectManagerId);
        try {
            boolean deleted = projectManagerService.deleteProjectManager(projectManagerId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Project manager deleted successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Failed to delete project manager. Project manager not found."));
            }
        } catch (Exception e) {
            log.error("Error deleting project manager with ID: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error."));
        }
    }

    @PostMapping("/{project-manager-id}/verify-pwd")
    public ResponseEntity<?> verifyProjectManagerPassword(@PathVariable("project-manager-id") Long projectManagerId, @RequestBody ProjectManagerDto projectManagerDto) {
        try{
            log.info("Check project manager pwd");
            boolean success = projectManagerService.verifyPassword(projectManagerId, projectManagerDto.getPwd());
            if(success)
                return ResponseEntity.ok(Map.of("message", "Password verified success"));
            else
                return ResponseEntity.badRequest().body(Map.of("message", "Password verified fail"));
        } catch (Exception e) {
            log.error("Error verified project manager pwd: {}", projectManagerId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error."));
        }
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("message", "No user found with the given id."));
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid request."));
    }
}