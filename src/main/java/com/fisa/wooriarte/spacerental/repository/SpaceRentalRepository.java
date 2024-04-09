package com.fisa.wooriarte.spacerental.repository;


import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceRentalRepository extends JpaRepository<SpaceRental, Long> {
    //고유번호가 아닌 ID로 검색
    @Query(value = "select * from space_rental s where s.id = :id", nativeQuery = true)
    Optional<SpaceRental> findBySpaceRentalId(String id);
}
