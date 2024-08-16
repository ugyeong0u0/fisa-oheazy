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

@RestController
@RequestMapping("/api/space-rentals")
public class SpaceRentalController {
    private final SpaceRentalService spaceRentalService;
    private static final Logger log = LoggerFactory.getLogger(SpaceRentalController.class);

    @Autowired
    public SpaceRentalController(SpaceRentalService spaceRentalService) {
        this.spaceRentalService = spaceRentalService;
    }

    // 임대사업자 등록
    @PostMapping("")
    public ResponseEntity<String> addSpaceRental(@RequestBody SpaceRentalDto spaceRentalDTO) {
        log.info("새로운 임대사업자 등록 시도");
        try {
            if (spaceRentalService.addSpaceRental(spaceRentalDTO)) {
                log.info("임대사업자 등록 성공");
                return ResponseEntity.ok("임대사업자 등록 성공");
            } else {
                log.warn("임대사업자 등록 실패");
                return ResponseEntity.badRequest().body("임대사업자 등록 실패");
            }
        } catch (DataIntegrityViolationException e) {
            log.error("데이터 무결성 위반으로 등록 실패", e);
            return ResponseEntity.badRequest().body("데이터 무결성 위반으로 인한 등록 실패");
        }
    }

    // 임대사업자 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginSpaceRental(@RequestBody SpaceRentalDto loginInfo) {
        log.info("ID {}로 로그인 시도", loginInfo.getSpaceRentalId());
        try {
            String id = loginInfo.getSpaceRentalId().toString();
            String pwd = loginInfo.getPwd();
            SpaceRentalDto spaceRentalDto = spaceRentalService.loginSpaceRental(id, pwd);
            return ResponseEntity.ok(spaceRentalDto.getSpaceRentalId().toString());
        } catch (NoSuchElementException e) {
            log.error("로그인 실패 - 사용자 없음: {}", loginInfo.getSpaceRentalId(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            log.error("로그인 중 오류 발생: {}", loginInfo.getSpaceRentalId(), e);
            return ResponseEntity.internalServerError().body("로그인 중 오류 발생");
        }
    }

    // JWT 로그인
    @PostMapping("/jwtlogin")
    public ResponseEntity<JwtToken> login(@RequestBody UserLoginRequestDto userLoginRequestDTO) {
        try {
            log.info("JWT로 로그인 시도");
            String id = userLoginRequestDTO.getId();
            String pwd = userLoginRequestDTO.getPwd();
            JwtToken token = spaceRentalService.login(id, pwd);
            return ResponseEntity.ok(token);
        } catch (NoSuchElementException e) {
            log.error("JWT 로그인 실패 - 사용자 없음: {}", userLoginRequestDTO.getId(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("JWT 로그인 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 임대사업자 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<String> findSpaceRentalId(@RequestBody String email) {
        log.info("이메일 {}로 ID 찾기 시도", email);
        try {
            String id = spaceRentalService.getId(email);
            log.info("ID 찾기 성공 - 이메일: {}", email);
            return ResponseEntity.ok(id);
        } catch (NoSuchElementException e) {
            log.error("ID 찾기 실패 - 이메일 없음: {}", email, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID를 찾을 수 없습니다");
        } catch (Exception e) {
            log.error("ID 찾기 중 예외 발생: {}", email, e);
            return ResponseEntity.internalServerError().body("ID 찾기 중 오류 발생");
        }
    }

    // 임대사업자 비밀번호 재설정
    @PostMapping("/set-pwd")
    public ResponseEntity<String> findSpaceRentalPw(@RequestBody Map<String, String> pwdInfo) {
        String id = pwdInfo.get("id");
        String newPwd = pwdInfo.get("new_pwd");
        log.info("ID {}에 대한 비밀번호 재설정 시도", id);
        try {
            if (spaceRentalService.setPwd(id, newPwd)) {
                log.info("ID {}에 대한 비밀번호 재설정 성공", id);
                return ResponseEntity.ok("비밀번호 재설정 성공");
            } else {
                log.warn("ID {}에 대한 비밀번호 재설정 실패", id);
                return ResponseEntity.badRequest().body("비밀번호 재설정 실패");
            }
        } catch (NoSuchElementException e) {
            log.error("비밀번호 재설정 실패 - 사용자 없음: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("비밀번호 재설정 중 오류 발생: {}", id, e);
            return ResponseEntity.internalServerError().body("비밀번호 재설정 중 오류 발생");
        }
    }

    // 임대사업자 정보 조회
    @GetMapping("/{space-rental-id}")
    public ResponseEntity<SpaceRentalDto> getSpaceRentalInfo(@PathVariable("space-rental-id") Long spaceRentalId) {
        log.info("ID {}에 대한 임대사업자 정보 조회 시도", spaceRentalId);
        try {
            SpaceRentalDto spaceRentalDTO = spaceRentalService.findBySpaceRentalId(spaceRentalId);
            log.info("ID {}에 대한 임대사업자 정보 조회 성공", spaceRentalId);
            return ResponseEntity.ok(spaceRentalDTO);
        } catch (NoSuchElementException e) {
            log.error("ID {}에 대한 임대사업자 정보 조회 실패 - 사용자 없음", spaceRentalId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("ID {}에 대한 임대사업자 정보 조회 중 오류 발생", spaceRentalId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 임대사업자 정보 갱신
    @PutMapping("/{space-rental-id}")
    public ResponseEntity<String> updateSpaceRentalInfo(@PathVariable("space-rental-id") Long spaceRentalId, @RequestBody SpaceRentalDto spaceRentalDTO) {
        log.info("ID {}에 대한 임대사업자 정보 갱신 시도", spaceRentalId);
        try {
            if (spaceRentalService.updateSpaceRental(spaceRentalId, spaceRentalDTO)) {
                log.info("ID {}에 대한 임대사업자 정보 갱신 성공", spaceRentalId);
                return ResponseEntity.ok("임대사업자 정보 갱신 성공");
            } else {
                log.warn("ID {}에 대한 임대사업자 정보 갱신 실패", spaceRentalId);
                return ResponseEntity.badRequest().body("임대사업자 정보 갱신 실패");
            }
        } catch (NoSuchElementException e) {
            log.error("정보 갱신 실패 - 사용자 없음: {}", spaceRentalId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("ID {}에 대한 임대사업자 정보 갱신 중 오류 발생", spaceRentalId, e);
            return ResponseEntity.internalServerError().body("임대사업자 정보 갱신 중 오류 발생");
        }
    }

    // 임대사업자 삭제
    @DeleteMapping("/{space-rental-id}")
    public ResponseEntity<String> deleteSpaceRental(@PathVariable("space-rental-id") Long spaceRentalId) {
        log.info("ID {}에 대한 임대사업자 삭제 시도", spaceRentalId);
        try {
            if (spaceRentalService.deleteSpaceRental(spaceRentalId)) {
                log.info("ID {}에 대한 임대사업자 삭제 성공", spaceRentalId);
                return ResponseEntity.ok("임대사업자 삭제 성공");
            } else {
                log.warn("ID {}에 대한 임대사업자 삭제 실패", spaceRentalId);
                return ResponseEntity.badRequest().body("임대사업자 삭제 실패");
            }
        } catch (NoSuchElementException e) {
            log.error("삭제 실패 - 사용자 없음: {}", spaceRentalId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("ID {}에 대한 임대사업자 삭제 중 오류 발생", spaceRentalId, e);
            return ResponseEntity.internalServerError().body("임대사업자 삭제 중 오류 발생");
        }
    }

    // 임대사업자 비밀번호 확인
    @PostMapping("/{space-rental-id}/verify-pwd")
    public ResponseEntity<String> verifySpaceRentalPassword(@PathVariable("space-rental-id") Long spaceRentalId, @RequestBody SpaceRentalDto spaceRentalDto) {
        try {
            log.info("ID {}에 대한 비밀번호 확인 시도", spaceRentalId);
            boolean success = spaceRentalService.verifyPassword(spaceRentalId, spaceRentalDto.getPwd());
            return success ? ResponseEntity.ok("비밀번호 확인 성공") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 확인 실패");
        } catch (NoSuchElementException e) {
            log.error("비밀번호 확인 실패 - 사용자 없음: {}", spaceRentalId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        } catch (Exception e) {
            log.error("비밀번호 확인 중 오류 발생 - ID: {}", spaceRentalId, e);
            return ResponseEntity.internalServerError().body("비밀번호 확인 중 오류 발생");
        }
    }
}