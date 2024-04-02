package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.exception.MessageException;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;


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
    public boolean createUser(UserDTO userDTO)  {
        User userEntity = userDTO.toEntity();
        User userEmail = userRepository.findUserByEmail(userEntity.getEmail());
        if (userEmail == null) {
            userRepository.save(userEntity);
            System.out.println("회원가입 가능");
            return true;
        }
        System.out.println("회원가입 불가능 (이메일 중복)");
        return false;

    }
}
    
