package com.fisa.wooriarte.spacerental.repository;


import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpaceRentalRepository extends JpaRepository<SpaceRental, Long> {
    @Query(value = "select * from space_rental s where s.id = :id", nativeQuery = true)
    SpaceRental findBySpaceRentalId(String id);
}
