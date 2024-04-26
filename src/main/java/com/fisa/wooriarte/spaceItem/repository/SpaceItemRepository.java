package com.fisa.wooriarte.spaceItem.repository;

import com.fisa.wooriarte.spaceItem.domain.City;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceItemRepository extends JpaRepository<SpaceItem, Long> {

    Optional<SpaceItem> findBySpaceItemIdAndIsDeletedFalse(Long id);
    Optional<List<SpaceItem>> findBySpaceRental(SpaceRental spaceRental);
    Optional<List<SpaceItem>> findAllByIsDeletedFalse();

    Optional<List<SpaceItem>> findByStartDateGreaterThanEqualAndEndDateLessThanEqualAndCity(LocalDate startDate, LocalDate endDate, City city);
}
