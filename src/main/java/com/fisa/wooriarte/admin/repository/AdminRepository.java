package com.fisa.wooriarte.admin.repository;

import com.fisa.wooriarte.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository  extends JpaRepository<Admin, Integer> {
    Optional<Admin> findById(String id);
}
