package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.jwt.JwtTokenProvider;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDto;
import com.fisa.wooriarte.user.dto.request.UserInfoRequestDto;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    // 로직 boolean으로 반환
    public boolean addUser(UserDto userDto) {
        //userDTO.toEntity 를 User userEntity로 변환한 이유는
        //클라이언트로부터 받은 데이터를 db에 저장하기 위함
        User userEntity = userDto.toEntity();
        Optional<User> userEmail = userRepository.findByEmail(userEntity.getEmail());

        if (userEmail.isPresent() && !userEmail.get().getIsDeleted()) {
            System.out.println("유저 삭제여부:" + userEmail.get().getIsDeleted());
            throw new IllegalStateException("회원가입 불가능(이메일 중복)");
        }

        Optional<User> userId = userRepository.findById(userEntity.getId());

        if (userId.isPresent()&&!userEmail.get().getIsDeleted()) {
            throw new IllegalStateException("회원가입 불가능 (아이디 중복)");
        }

        userEntity.addRole("USER");
        userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
        userRepository.save(userEntity);
        System.out.println("회원가입 가능");

        return true;
    }

    //비밀번호 검증
    public boolean verifyPassword(Long id, String password) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));

        if (passwordEncoder.matches(user.getPwd(), password)) {
            System.out.println("비밀번호 일치");
            return true; //비밀번호 일치 -> true 반환
        } else {
            return false; //비밀번호 불일치 -> false 반환
        }

    }

    // 유저 개인 정보 단건 조회
    public UserDto getUserInfo(Long userId) {
        try {
            final User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));
            return UserDto.fromEntity(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 로그인
    public UserDto loginUser(String id, String pwd) throws NoSuchElementException, IllegalAccessException {
        Optional<User> optionalUser = userRepository.findById(id);
        UserDto userdto = null;
        if (passwordEncoder.matches(pwd, optionalUser.get().getPwd())) {
            userdto = UserDto.fromEntity(optionalUser.get());
            return userdto;
        } else {
            throw new IllegalAccessException("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    @Transactional
    public JwtToken login(String id, String pwd) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행

        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 로깅
            throw e; // 필요한 경우, 예외를 다시 던져 처리할 수 있습니다.
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }
    
    // 유저 개인 정보 수정
    public Boolean updateMyUser(Long id, UserInfoRequestDto userInfoRequestDto) {
        // 이메일 중복 확인
        Optional<User> userEmail = userRepository.findByEmail(userInfoRequestDto.getEmail());
        if (userEmail.isPresent()) {
            System.out.println("수정 불가능(이메일 중복)");
            return false;
        }

        // 이메일이 중복되지 않은 경우, 엔터티 업데이트
        final int result = userRepository.updateAllById(id, userInfoRequestDto.getId(),
                userInfoRequestDto.getPwd(), userInfoRequestDto.getName(), userInfoRequestDto.getEmail(), userInfoRequestDto.getPhone());
        System.out.println("변경된 엔터티 개수" + result);
        return true;
    }

    // 유저 아이디 찾기
    public String findUserId( String email)  {
        User userInfo = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("유저 아이디를 찾을 수 없습니다.")); //객체 없으면 에러던지기

        return userInfo.getId(); //아이디 던저주기
    }

    // 유저 비밀번호 찾기
    public String findUserPw (String id, String name, String email){
        User userInfo = userRepository.findByIdAndNameAndEmail(id, name, email)
                .orElseThrow(() -> new NoSuchElementException("유저 비밀번호를 찾을 수 없습니다.")); //객체 없으면 에러던지기

        return userInfo.getPwd(); //아이디 던저주기
    }


    //유저 삭제 ~
    //user_id 검색 후 delete 상태 변경
    @Transactional
    public void deleteUser (Long userId){
        //user_id로 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("없는 유저입니다."));
        if (user.getIsDeleted()) {
            throw new DataIntegrityViolationException("Already deleted User");
        }
        user.setIsDeleted();
        userRepository.save(user);
    }
}

