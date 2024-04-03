package com.fisa.wooriarte.user.repository;

import com.fisa.wooriarte.user.domain.User; //User Entity
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByEmail(String email); //유저 이메일 찾기


    Optional<User> findUserByid(String id); //유저 아이디 찾기
}
