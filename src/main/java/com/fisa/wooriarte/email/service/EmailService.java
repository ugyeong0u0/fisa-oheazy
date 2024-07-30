package com.fisa.wooriarte.email.service;

import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import com.fisa.wooriarte.redis.RedisUtil;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.ticket.domain.Ticket;
import com.fisa.wooriarte.ticket.repository.TicketRepository;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class EmailService {
    private final UserRepository userRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final SpaceRentalRepository spaceRentalRepository;
    private final TicketRepository ticketRepository;
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private int authNumber;

    @Autowired
    public EmailService(UserRepository userRepository,
                        ProjectManagerRepository projectManagerRepository,
                        SpaceRentalRepository spaceRentalRepository, TicketRepository ticketRepository,
                        JavaMailSender mailSender,
                        RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.spaceRentalRepository = spaceRentalRepository;
        this.ticketRepository = ticketRepository;
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
    }

    public boolean checkAuthNum(String email,String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkUserId(String id) {
        return userRepository.existsById(id);
    }

    public boolean checkProjectManagerId(String id) {
        return projectManagerRepository.existsById(id);
    }

    public boolean checkSpaceRentalId(String id) {
        return spaceRentalRepository.existsById(id);
    }

    //임의의 6자리 양수 반환
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "nowead814@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "Woori Arte 회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "Woori Arte" + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*3L);
    }

    public void ticketEmail(String email, String ticketNo, String exhibitName, Long ticketAmount, String exhibitTime) {

        String setFrom = "nowead814@gmail.com"; // 발신자 이메일 주소
        String toMail = email; // 수신자 이메일 주소
        String title = "Woori Arte 전시 티켓 구매가 완료되었습니다."; // 이메일 제목
        String content =
                "<h1>Woori Arte 전시 티켓 구매 확인</h1>" + // 이메일 내용 시작
                        "<br><br>" +
                        "안녕하세요, Woori Arte입니다. 귀하의 전시 티켓 구매가 성공적으로 완료되었습니다." +
                        "<br><br>" +
                        "구매하신 티켓 정보는 다음과 같습니다." +
                        "<br><br>" +
                        "<b>전시 이름:</b> " + exhibitName + "<br>" + // 전시 이름 파라미터 삽입
                        "<b>티켓 번호:</b> " + ticketNo + "<br>" + // 티켓 번호 파라미터 삽입
                        "<b>구매 티켓 수량:</b> " + ticketAmount.toString() + "장<br>" + // 티켓 금액 파라미터 삽입
                        "<b>전시 시간:</b> " + exhibitTime + "<br><br>" + // 전시 시간 파라미터 삽입
                        "다시 한번 Woori Arte 전시에 관심을 가져주셔서 감사드립니다." +
                        "<br><br>" +
                        "Woori Arte"; // 이메일 내용 마무리

        ticketEmailSend(setFrom, toMail, title, content);
    // 메일 발송 메소드 호출
    }

    public void ticketEmailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
    }


}