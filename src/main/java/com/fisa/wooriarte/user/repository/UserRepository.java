package com.fisa.wooriarte.user.repository;

import com.fisa.wooriarte.user.domain.User; //User Entity
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByEmail(String email); //유저 이메일 찾기
    User findUserByid(String id); //유저 아이디 찾기
}
