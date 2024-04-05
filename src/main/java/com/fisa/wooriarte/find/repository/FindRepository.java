package com.fisa.wooriarte.find.repository;

import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FindRepository extends JpaRepository<SpaceRental, Long> {
    // 아이디 찾기
    @Query(value = "select * from space_rental s where s.email = :email and s.business_number = :businessNumber", nativeQuery = true)
    Optional<SpaceRental> findBySpaceRentalId(@Param("email") String email, @Param("businessNumber") Long businessNumber);

    // 패스워드 찾기
    @Query(value = "select * from space_rental s where s.email = :email and s.business_number = :businessNumber and s.id = :id", nativeQuery = true)
    Optional<SpaceRental> findBySpaceRentalId(@Param("email") String email, @Param("businessNumber") Long businessNumber, @Param("id") String id);
}
