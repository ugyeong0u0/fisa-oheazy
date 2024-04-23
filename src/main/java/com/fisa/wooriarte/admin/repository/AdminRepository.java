package com.fisa.wooriarte.admin.repository;

import com.fisa.wooriarte.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository  extends JpaRepository<Admin, Integer> {
}
