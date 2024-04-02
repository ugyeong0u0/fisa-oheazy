package com.fisa.wooriarte.user.repository;

import com.fisa.wooriarte.user.domain.User; //User Entity
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByEmail(String email);
}
