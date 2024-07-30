package com.fisa.wooriarte.user.repository;

import com.fisa.wooriarte.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email); //유저 이메일 찾기
    Optional<User> findById(String id); //유저 아이디 찾기
//    Optional<User> findByEmail(String email); // 유저 이름, 이메일 찾기
    Optional<User> findByIdAndNameAndEmail(String id, String name, String email); // 유저 이름, 이메일 찾기
    Boolean existsById(String id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.id = :id, u.pwd = :pwd, u.name = :name, u.email = :email, u.phone = :phone WHERE u.userId = :userId")
    int updateAllById(
            @Param("userId") Long userId,
            @Param("id") String id,
            @Param("pwd") String pwd,
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone
    );
}
