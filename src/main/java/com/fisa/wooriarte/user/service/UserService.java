package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
            System.out.println("회원가입 불가능 (이메일 중복)");
            return false;
        }

        Optional<User> userId = userRepository.findUserByid(userEntity.getId());
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
        User user = userRepository.findUserByid(id)
                .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));

        if (userDTO.getPwd().equals(user.getPwd())) {
            System.out.println("비밀번호 일치");
            return true; //비밀번호 일치 -> true 반환
        } else {
            return false; //비밀번호 불일치 -> false 반환
        }

    }

}

