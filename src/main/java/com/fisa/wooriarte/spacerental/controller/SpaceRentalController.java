package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDto;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/api/space-rentals")
public class SpaceRentalController {
    private final SpaceRentalService spaceRentalService;
    private static final Logger log = LoggerFactory.getLogger(SpaceRentalController.class);

    @Autowired
    public SpaceRentalController(SpaceRentalService spaceRentalService) {
        this.spaceRentalService = spaceRentalService;
    }

    @PostMapping("")
    public ResponseEntity<?> addSpaceRental(@RequestBody SpaceRentalDto spaceRentalDTO) {
        log.info("Attempting to register new space rental");
        try {
            if(spaceRentalService.addSpaceRental(spaceRentalDTO)) {
                log.info("Space rental registration successful");
                return ResponseEntity.ok(Map.of("message", "Space rental registration successful."));
            } else {
                log.warn("Space rental registration failed");
                return ResponseEntity.badRequest().body(Map.of("message", "Space rental registration failed."));
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed due to data integrity violation", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Impossible request due to data integrity violation."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginSpaceRental(@RequestBody Map<String, String> loginInfo) {
        log.info("Attempting login for ID {}", loginInfo.get("id"));
        try {
            String id = loginInfo.get("id");
            String pwd = loginInfo.get("pwd");
            SpaceRentalDto spaceRentalDto= spaceRentalService.loginSpaceRental(id, pwd);
            return ResponseEntity.status(HttpStatus.OK).body(spaceRentalDto.getSpaceRentalId());

        } catch (Exception e) {
            log.error("Login failed for ID {}", loginInfo.get("id"), e);
            return ResponseEntity.badRequest().body(Map.of("message", "An error occurred during login."));
        }
    }

    @PostMapping("/jwtlogin")
    public JwtToken login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        String id = userLoginRequestDTO.getId();
        String pwd = userLoginRequestDTO.getPwd();
        return spaceRentalService.login(id, pwd);
    }

    // 공간대여자 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findSpaceRentalId(@RequestBody String email) {
        log.info("Attempting to find ID for email: {}", email);
        try {
            String id = spaceRentalService.getId(email);
            log.info("ID retrieval successful for email: {}", email);
            return ResponseEntity.ok(Map.of("message", "ID retrieval successful.", "id", id));
        } catch (NoSuchElementException e) {
            log.error("ID not found for email: {}", email, e);
            return ResponseEntity.badRequest().body(Map.of("message", "ID not found."));
        }
    }

    // 공간대여자 비밀번호 재설정
    @PostMapping("/set-pwd")
    public ResponseEntity<?> findSpaceRentalPw(@RequestBody Map<String, String> pwdInfo) {
        String id = pwdInfo.get("id");
        String newPwd = pwdInfo.get("new_pwd");
        log.info("Attempting to reset password for ID: {}", id);
        try {
            if(spaceRentalService.setPwd(id, newPwd)) {
                log.info("Password reset successful for ID: {}", id);
                return ResponseEntity.ok(Map.of("message", "Password reset successful."));
            } else {
                log.warn("Password reset failed for ID: {}", id);
                return ResponseEntity.badRequest().body(Map.of("message", "Password reset failed."));
            }
        } catch (Exception e) {
            log.error("An error occurred during password reset for ID: {}", id, e);
            return ResponseEntity.badRequest().body(Map.of("message", "An error occurred during password reset."));
        }
    }

    // 공간대여자 정보 조회
    @GetMapping("/{space_rental_id}")
    public ResponseEntity<?> getSpaceRentalInfo(@PathVariable("space_rental_id") Long spaceRentalId) {
        log.info("Fetching space rental info for ID: {}", spaceRentalId);
        try {
            SpaceRentalDto spaceRentalDTO = spaceRentalService.findBySpaceRentalId(spaceRentalId);
            log.info("Space rental info retrieval successful for ID: {}", spaceRentalId);
            return ResponseEntity.ok(Map.of("message", "Space rental info retrieval successful.", "data", spaceRentalDTO));
        } catch (NoSuchElementException e) {
            log.error("Space rental info not found for ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Space rental info not found."));
        }
    }

    // 공간대여자 정보 갱신
    @PutMapping("/{space_rental_id}")
    public ResponseEntity<?> updateSpaceRentalInfo(@PathVariable("space_rental_id") Long spaceRentalId, @RequestBody SpaceRentalDto spaceRentalDTO) {
        log.info("Attempting to update space rental information for ID: {}", spaceRentalId);
        try {
            if (spaceRentalService.updateSpaceRental(spaceRentalId, spaceRentalDTO)) {
                log.info("Space rental information successfully updated for ID: {}", spaceRentalId);
                return ResponseEntity.ok(Map.of("message", "Space rental information successfully updated"));
            } else {
                log.warn("Failed to update space rental information for ID: {}", spaceRentalId);
                return ResponseEntity.badRequest().body(Map.of("message", "Failed to update space rental information"));
            }
        } catch (Exception e) {
            log.error("Error occurred while updating space rental information for ID: {}", spaceRentalId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Error occurred while updating space rental information"));
        }
    }

    // 공간대여자 삭제
    @DeleteMapping("/{space_rental_id}")
    public ResponseEntity<?> deleteSpaceRental(@PathVariable("space_rental_id") Long spaceRentalId) {
        log.info("Attempting to delete space rental with ID {}", spaceRentalId);
        try {
            if (spaceRentalService.deleteSpaceRental(spaceRentalId)) {
                log.info("Space rental with ID {} successfully deleted", spaceRentalId);
                return ResponseEntity.ok(Map.of("message", "Space rental successfully deleted"));
            } else {
                log.warn("Failed to delete space rental with ID {}", spaceRentalId);
                return ResponseEntity.badRequest().body(Map.of("message", "Failed to delete space rental"));
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting space rental with ID {}", spaceRentalId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Error occurred while deleting space rental"));
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