package com.fisa.wooriarte.email.service;

import com.fisa.wooriarte.email.controller.EmailController;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import com.fisa.wooriarte.redis.RedisUtil;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Random;

@Service
public class EmailService {
    private final UserRepository userRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final SpaceRentalRepository spaceRentalRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final Logger log = LoggerFactory.getLogger(EmailController.class);
    private int authNumber;

    @Autowired
    public EmailService(UserRepository userRepository,
                        ProjectManagerRepository projectManagerRepository,
                        SpaceRentalRepository spaceRentalRepository,
                        JavaMailSender mailSender,
                        RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.spaceRentalRepository = spaceRentalRepository;
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
    }

    /**
     * 사용자 입력 이메일, 인증번호와 Redis 내 인증번호 일치 여부 확인
     * @param email 이메일 주소
     * @param authNum 인증번호
     * @return 인증 성공 여부
     */
    public boolean checkAuthNum(String email, String authNum) {
        String storedEmail = redisUtil.getData(authNum);
        return storedEmail != null && storedEmail.equals(email);
    }

    /**
     * 사용자 ID 존재 여부 확인
     * @param id 사용자 ID
     * @return ID 존재 여부
     */
    public boolean checkUserId(String id) {
        return userRepository.existsById(id);
    }

    /**
     * 프로젝트 매니저 ID의 존재 여부 확인
     * @param id 프로젝트 매니저 ID
     * @return ID 존재 여부
     */
    public boolean checkProjectManagerId(String id) {
        return projectManagerRepository.existsById(id);
    }

    /**
     * 임대사업자 ID 존재 여부 확인
     * @param id 임대사업자 ID
     * @return ID 존재 여부
     */
    public boolean checkSpaceRentalId(String id) {
        return spaceRentalRepository.existsById(id);
    }

    /**
     * 회원가입 인증 이메일 내용 생성
     * @param email 수신자 이메일 주소
     * @return 생성된 인증번호
     */
    public String joinEmail(String email) {
        try {
            makeRandomNumber();
            String setFrom = "nowead814@gmail.com";
            String title = "Woori Arte 회원 가입 인증 이메일 입니다.";
            String content = "Woori Arte<br><br>인증 번호는 " + authNumber + "입니다.<br>";
            mailSend(setFrom, email, title, content);
            return Integer.toString(authNumber);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    /**
     * 이메일 전송 및 Redis에 인증번호, 이메일 저장
     * @param setFrom 발신자 이메일 주소
     * @param toMail 수신자 이메일 주소
     * @param title 이메일 제목
     * @param content 이메일 내용
     * @throws MessagingException 이메일 전송 중 오류가 발생할 경우
     */
    public void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(setFrom);
        helper.setTo(toMail);
        helper.setSubject(title);
        helper.setText(content, true);
        mailSender.send(message);
        redisUtil.setDataExpire(Integer.toString(authNumber), toMail, 60 * 3L); // 인증번호를 Redis에 3분간 저장
    }

    /**
     * 랜덤 6자리 인증번호를 생성
     */
    private void makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber.toString());
    }

    /**
     * 티켓 내용 생성 및 전송
     * @param email 수신자 이메일 주소
     * @param ticketNo 티켓 번호
     * @param exhibitName 전시 이름
     * @param ticketAmount 티켓 수량
     * @param exhibitTime 전시 시간
     */
    public void ticketEmail(String email, String ticketNo, String exhibitName, Long ticketAmount, String exhibitTime) {
        try {
            String setFrom = "nowead814@gmail.com";
            String title = "Woori Arte 전시 티켓 구매가 완료되었습니다.";
            String content = "<h1>Woori Arte 전시 티켓 구매 확인</h1><br><br>" +
                    "안녕하세요, Woori Arte입니다. 귀하의 전시 티켓 구매가 성공적으로 완료되었습니다.<br><br>" +
                    "구매하신 티켓 정보는 다음과 같습니다.<br><br>" +
                    "<b>전시 이름:</b> " + exhibitName + "<br>" +
                    "<b>티켓 번호:</b> " + ticketNo + "<br>" +
                    "<b>구매 티켓 수량:</b> " + ticketAmount + "장<br>" +
                    "<b>전시 시간:</b> " + exhibitTime + "<br><br>" +
                    "다시 한번 Woori Arte 전시에 관심을 가져주셔서 감사드립니다.<br><br>Woori Arte";
            mailSend(setFrom, email, title, content);
        } catch (MessagingException e) {
            log.error("티켓 이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("티켓 이메일 전송 실패", e);
        }
    }
}