package com.fisa.wooriarte.spaceItem.repository;

import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceItemRepository extends JpaRepository<SpaceItem, Long> {
<<<<<<< HEAD
=======

>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
    Optional<SpaceItem> findBySpaceItemIdAndIsDeletedFalse(Long id);
}
