package com.fisa.wooriarte.spacerental.repository;


import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpaceRentalRepository extends JpaRepository<SpaceRental, Long> {
    //고유번호가 아닌 ID로 검색
    Optional<SpaceRental> findBySpaceRentalId(String id);
}
