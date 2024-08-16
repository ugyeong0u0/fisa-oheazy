package com.fisa.wooriarte.email.controller;

import com.fisa.wooriarte.email.dto.EmailIdCheckDto;
import com.fisa.wooriarte.email.dto.EmailPwdCheckDto;
import com.fisa.wooriarte.email.dto.EmailFindIdDto;
import com.fisa.wooriarte.email.dto.EmailSetPwdDto;
import com.fisa.wooriarte.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // 이메일 인증 요청 처리
    @PostMapping("/email-send")
    public ResponseEntity<String> emailSend(@RequestBody @Valid EmailFindIdDto emailDto) {
        log.info("이메일 인증 요청 - 이메일: {}", emailDto.getEmail());
        try {
            String result = emailService.joinEmail(emailDto.getEmail());
            log.info("이메일 전송 성공 - 이메일: {}", emailDto.getEmail());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("이메일 전송 실패 - 이메일: {}", emailDto.getEmail(), e);
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    // 이메일 인증 번호 확인 처리
    @PostMapping("/email-auth-check")
    public ResponseEntity<String> authCheck(@RequestBody @Valid EmailIdCheckDto emailPwdCheckDto) {
        try {
            // 인증번호 확인
            boolean isChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            log.info("이메일 인증 {} - 이메일: {}", isChecked ? "성공" : "실패", emailPwdCheckDto.getEmail());
            return isChecked ? ResponseEntity.ok("인증 성공") : ResponseEntity.badRequest().body("인증 번호 불일치");
        } catch (Exception e) {
            log.error("이메일 인증 중 오류 발생 - 이메일: {}", emailPwdCheckDto.getEmail(), e);
            return ResponseEntity.badRequest().body("인증 처리 중 오류 발생");
        }
    }

    // 사용자 비밀번호 재설정용 이메일 인증
    @PostMapping("/users/email-send")
    public ResponseEntity<String> userEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("사용자 이메일 인증 요청 - 이메일: {}", emailDto.getEmail());
        try {
            // 사용자 ID 확인
            boolean isIdChecked = emailService.checkUserId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("이메일 전송 성공 - 이메일: {}", emailDto.getEmail());
                return ResponseEntity.ok(result);
            } else {
                log.warn("사용자 ID 확인 실패 - ID: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("존재하지 않는 사용자 ID입니다.");
            }
        } catch (Exception e) {
            log.error("이메일 전송 실패 - 이메일: {}", emailDto.getEmail(), e);
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    // 사용자 비밀번호 재설정용 인증번호 확인
    @PostMapping("/users/email-auth-check")
    public ResponseEntity<String> userAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto) {
        try {
            // 사용자 ID와 인증번호 확인
            boolean isIdChecked = emailService.checkUserId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if (isIdChecked && isAuthNumChecked) {
                log.info("사용자 인증 성공 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.ok("인증 성공");
            } else {
                log.warn("사용자 인증 실패 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.badRequest().body("입력 정보 불일치");
            }
        } catch (Exception e) {
            log.error("사용자 인증 중 오류 발생 - ID: {}", emailPwdCheckDto.getId(), e);
            return ResponseEntity.badRequest().body("인증 처리 중 오류 발생");
        }
    }

    // 프로젝트 매니저 비밀번호 재설정용 이메일 인증

    @PostMapping("/project-managers/email-send")
    public ResponseEntity<String> projectManagerEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("프로젝트 매니저 이메일 인증 요청 - 이메일: {}", emailDto.getEmail());
        try {
            // 프로젝트 매니저 ID 확인
            boolean isIdChecked = emailService.checkProjectManagerId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("이메일 전송 성공 - 이메일: {}", emailDto.getEmail());
                return ResponseEntity.ok(result);
            } else {
                log.warn("프로젝트 매니저 ID 확인 실패 - ID: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("존재하지 않는 프로젝트 매니저 ID입니다.");
            }
        } catch (Exception e) {
            log.error("이메일 전송 실패 - 이메일: {}", emailDto.getEmail(), e);
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    // 프로젝트 매니저 비밀번호 재설정용 인증번호 확인
    @PostMapping("/project-managers/email-auth-check")
    public ResponseEntity<String> projectManagerAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto) {
        try {
            // 프로젝트 매니저 ID와 인증번호 확인
            boolean isIdChecked = emailService.checkProjectManagerId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if (isIdChecked && isAuthNumChecked) {
                log.info("프로젝트 매니저 인증 성공 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.ok("인증 성공");
            } else {
                log.warn("프로젝트 매니저 인증 실패 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.badRequest().body("입력 정보 불일치");
            }
        } catch (Exception e) {
            log.error("프로젝트 매니저 인증 중 오류 발생 - ID: {}", emailPwdCheckDto.getId(), e);
            return ResponseEntity.badRequest().body("인증 처리 중 오류 발생");
        }
    }

    // 임대사업자 비밀번호 재설정용 이메일 인증
    @PostMapping("/space-rentals/email-send")
    public ResponseEntity<String> spaceRentalEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("임대사업자 이메일 인증 요청 - 이메일: {}", emailDto.getEmail());
        try {
            // 임대사업자 ID 확인
            boolean isIdChecked = emailService.checkSpaceRentalId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("이메일 전송 성공 - 이메일: {}", emailDto.getEmail());
                return ResponseEntity.ok(result);
            } else {
                log.warn("임대사업자 ID 확인 실패 - ID: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("존재하지 않는 임대사업자 ID입니다.");
            }
        } catch (Exception e) {
            log.error("이메일 전송 실패 - 이메일: {}", emailDto.getEmail(), e);
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }


    // 임대사업자 비밀번호 재설정용 인증번호 확인
    @PostMapping("/space-rentals/email-auth-check")
    public ResponseEntity<String> spaceRentalAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto) {
        try {
            // 임대사업자 ID와 인증번호 확인
            boolean isIdChecked = emailService.checkSpaceRentalId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if (isIdChecked && isAuthNumChecked) {
                log.info("임대사업자 인증 성공 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.ok("인증 성공");
            } else {
                log.warn("임대사업자 인증 실패 - ID: {}", emailPwdCheckDto.getId());
                return ResponseEntity.badRequest().body("입력 정보 불일치");
            }
        } catch (Exception e) {
            log.error("임대사업자 인증 중 오류 발생 - ID: {}", emailPwdCheckDto.getId(), e);
            return ResponseEntity.badRequest().body("인증 처리 중 오류 발생");
        }
    }
}