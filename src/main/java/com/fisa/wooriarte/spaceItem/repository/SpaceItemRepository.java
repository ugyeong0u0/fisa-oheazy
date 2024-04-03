package com.fisa.wooriarte.spaceItem.repository;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceItemRepository extends JpaRepository<SpaceItem, Long> {

    Optional<SpaceItem> findById(Long id);
}
