package com.fisa.wooriarte.exhibit.repository;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit,Long> {
        @Query(value = "select * from exhibit e where e.exhibit_id = :id", nativeQuery = true)
        Optional<Exhibit> findByExhibitId(String id);
}
