package com.fisa.wooriarte.user.service;


import com.fisa.wooriarte.exception.MessageException;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;

import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.dto.request.UserInfoRequest;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Spring Security를 사용하여 비밀번호를 인코딩하고 비교하기 위해 PasswordEncoder 의존성 추가
    //;; Bean등록 해야함 ;;
    //일단보류
//    @Autowired
//    private PasswordEncoder passwordEncoder;


//    ----------------------------------------------------


    //회원가입 로직 boolean으로 반환
    public boolean addUser(UserDTO userDTO) {
        //userDTO.toEntity 를 User userEntity로 변환한 이유는
        //클라이언트로부터 받은 데이터를 db에 저장하기 위함
        User userEntity = userDTO.toEntity();

        Optional<User> userEmail = userRepository.findUserByEmail(userEntity.getEmail());
        if (userEmail.isPresent()) {
            System.out.println("회원가입 불가능(이메일 중복)");
            return false;
        }

        Optional<User> userId = userRepository.findUserById(userEntity.getId());
        if (userId.isPresent()) {
            System.out.println("회원가입 불가능 (아이디 중복");
            return false;
        }

        userRepository.save(userEntity);
        System.out.println("회원가입 가능");
        return true;
    }


    //비밀번호 검증
    public boolean verifyPassword(String id, UserDTO userDTO) throws Exception {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));

        if (userDTO.getPwd().equals(user.getPwd())) {
            System.out.println("비밀번호 일치");
            return true; //비밀번호 일치 -> true 반환
        } else {
            return false; //비밀번호 불일치 -> false 반환
        }

    }


    // 유저 개인 정보 단건 조회
    public UserDTO getMyUser(Long userId) {
        try {
            final User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));
            return user.toDto();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 로그인
    public boolean loginUser(String id, String pwd) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        return optionalUser.isPresent() && optionalUser.get().getPwd().equals(pwd);
    }

    
    // 유저 개인 정보 수정
    public Boolean updateMyUser(Long id, UserInfoRequest userInfoRequest) {
        try {
            final int result = userRepository.updateAllById(id, userInfoRequest.getId(),
                    userInfoRequest.getPwd(), userInfoRequest.getName(), userInfoRequest.getEmail(), userInfoRequest.getPhone());
            System.out.println("변경된 엔터티 개수" + result);
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 아이디 찾기
    public String findUserId(String name, String email)  {
        User userInfo = userRepository.findUserByNameAndEmail(name, email)
                .orElseThrow(() -> new NoSuchElementException("유저 아이디를 찾을 수 없습니다.")); //객체 없으면 에러던지기

        return userInfo.getId(); //아이디 던저주기
    }

    // 유저 비밀번호 찾기
    public String findUserPw(String id, String name, String email)  {
        User userInfo = userRepository.findUserByIdAndNameAndEmail(id, name, email)
                .orElseThrow(() -> new NoSuchElementException("유저 비밀번호를 찾을 수 없습니다.")); //객체 없으면 에러던지기

        return userInfo.getPwd(); //아이디 던저주기
    }
}

