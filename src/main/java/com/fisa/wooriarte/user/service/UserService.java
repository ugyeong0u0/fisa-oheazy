package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.exception.MessageException;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //단순 유저생성 테스트
    public User create(UserDTO userDTO) {
        return userRepository.save(userDTO.toEntity());
    }

    //1. 회워가입 로직 boolean으로 반환
    //true -> 가입 가능
    //false -> 가입 불가능
    public boolean createUser(UserDTO userDTO) {
        //userDTO.toEntity 를 User userEntity로 변환한 이유는
        //클라이언트로부터 받은 데이터를 db에 저장하기 위함
        User userEntity = userDTO.toEntity();
        Optional<User> userEmail = userRepository.findUserByEmail(userEntity.getEmail());
        Optional<User> userId = userRepository.findUserById(userEntity.getId());

        //중복 아이디 or 이메일이 없다면 회원 등록 후 true를 반환
        if (userId != null){
            System.out.println("회원가입 불가능 (아이디 중복)");
            return false; //불가능 -> false 반환
        } else if (userEmail != null){
            System.out.println("회원가입 불가능 (이메일 중복)");
            return false; //불가능 -> false 반환
        } else {
            userRepository.save(userEntity);
            System.out.println("회원가입 가능");
            return true; //가능 -> true를 반환
        }

    }

    public boolean loginUser(String id, String pwd) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        return optionalUser.isPresent() && optionalUser.get().getPwd().equals(pwd);
    }
}
    
