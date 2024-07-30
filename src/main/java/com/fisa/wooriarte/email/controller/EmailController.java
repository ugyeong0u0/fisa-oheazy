package com.fisa.wooriarte.email.controller;

import com.fisa.wooriarte.email.dto.EmailIdCheckDto;
import com.fisa.wooriarte.email.dto.EmailPwdCheckDto;
import com.fisa.wooriarte.email.dto.EmailFindIdDto;
import com.fisa.wooriarte.email.dto.EmailSetPwdDto;
import com.fisa.wooriarte.email.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(EmailController.class);

    public EmailController(EmailService mailService) {
        this.emailService = mailService;
    }

    @PostMapping("/email-send")
    public ResponseEntity<String> emailSend(@RequestBody @Valid EmailFindIdDto emailDto){
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());
        try {
            String result = emailService.joinEmail(emailDto.getEmail());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("이메일 전송 실패: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/email-auth-check")
    public ResponseEntity<?> authCheck(@RequestBody @Valid EmailIdCheckDto emailPwdCheckDto){
        try {
            boolean Checked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if(Checked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "인증 번호 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "뭔가 잘못됐습니다: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/email-send")
    public ResponseEntity<String> userEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("Email verification request received.");
        log.info("Email for verification: {}", emailDto.getEmail());
        try {
            boolean isIdChecked = emailService.checkUserId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("Email sent successfully to: {}", emailDto.getEmail());
                return ResponseEntity.ok(result); // Success response
            } else {
                log.error("User ID not found: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("User ID does not exist."); // User ID not found
            }
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Email sending failed: " + e.getMessage()); // Failure response
        }
    }

    @PostMapping("/users/email-auth-check")
    public ResponseEntity<?> userAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto){
        try {
            boolean isIdChecked = emailService.checkUserId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if(isIdChecked & isAuthNumChecked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "입력 정보 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "에러 발생" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/project-managers/email-send")
    public ResponseEntity<String> projectManagerEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("Email verification request received.");
        log.info("Email for verification: {}", emailDto.getEmail());
        try {
            boolean isIdChecked = emailService.checkProjectManagerId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("Email sent successfully to: {}", emailDto.getEmail());
                return ResponseEntity.ok(result); // Success response
            } else {
                log.error("Project Manager ID not found: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("Project Manager ID does not exist."); // User ID not found
            }
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Email sending failed: " + e.getMessage()); // Failure response
        }
    }

    @PostMapping("/project-managers/email-auth-check")
    public ResponseEntity<?> projectManagerAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto){
        try {
            boolean isIdChecked = emailService.checkProjectManagerId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if(isIdChecked & isAuthNumChecked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "입력 정보 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "에러 발생" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/space-rentals/email-send")
    public ResponseEntity<String> spaceRentalEmailSend(@RequestBody @Valid EmailSetPwdDto emailDto) {
        log.info("Email verification request received.");
        log.info("Email for verification: {}", emailDto.getEmail());
        try {
            boolean isIdChecked = emailService.checkSpaceRentalId(emailDto.getId());
            if (isIdChecked) {
                String result = emailService.joinEmail(emailDto.getEmail());
                log.info("Email sent successfully to: {}", emailDto.getEmail());
                return ResponseEntity.ok(result); // Success response
            } else {
                log.error("Space Rental ID not found: {}", emailDto.getId());
                return ResponseEntity.badRequest().body("Space Rental ID does not exist."); // User ID not found
            }
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Email sending failed: " + e.getMessage()); // Failure response
        }
    }

    @PostMapping("/space-rentals/email-auth-check")
    public ResponseEntity<?> spaceRentalAuthCheck(@RequestBody @Valid EmailPwdCheckDto emailPwdCheckDto){
        try {
            boolean isIdChecked = emailService.checkSpaceRentalId(emailPwdCheckDto.getId());
            boolean isAuthNumChecked = emailService.checkAuthNum(emailPwdCheckDto.getEmail(), emailPwdCheckDto.getAuthNum());
            if(isIdChecked & isAuthNumChecked){
                return ResponseEntity.ok(Map.of("message", "인증 성공"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "입력 정보 불일치"));
            }
        } catch (Exception e) {
            // 일반적인 예외 처리
            return new ResponseEntity<>(Map.of("error", "에러 발생" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}