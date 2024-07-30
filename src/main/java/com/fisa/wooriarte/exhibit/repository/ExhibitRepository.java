package com.fisa.wooriarte.exhibit.repository;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitRepository extends JpaRepository<Exhibit,Long> {

}
