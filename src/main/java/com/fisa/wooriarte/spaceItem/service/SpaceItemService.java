package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpaceItemService {
    private final SpaceItemRepository spaceItemRepository;
    private final SpaceRentalRepository spaceRentalRepository;

    @Autowired
    public SpaceItemService(SpaceItemRepository spaceItemRepository, SpaceRentalRepository spaceRentalRepository) {
        this.spaceItemRepository = spaceItemRepository;
        this.spaceRentalRepository = spaceRentalRepository;
    }

    @Transactional
    public boolean addSpaceItem(SpaceItemDto spaceItemDTO){
        System.out.println("addSpaceItem");
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceItemDTO.getSpaceRentalId())
                .orElseThrow(() -> new NoSuchElementException("No space Rental"));
        spaceItemRepository.save(spaceItemDTO.toEntity(spaceRental));
        return true;
    }

    public List<SpaceItemDto> findAll() {
        System.out.println("findAll");
        return spaceItemRepository.findAll().stream()
                .map(SpaceItemDto::fromEntity) // 람다식을 사용하여 각 SpaceItem 엔티티를 SpaceItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<SpaceItemDto> findSpaceItemById(Long spaceItemId) {
        System.out.println("findSpaceItemById");
        return spaceItemRepository.findById(spaceItemId)
                .map(SpaceItemDto::fromEntity);

    }
    @Transactional
    public void updateSpaceItem(Long spaceItemId, SpaceItemDto spaceItemDTO) {
        System.out.println("updateSpaceItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId)
                .orElseThrow(() -> new NoSuchElementException(""));
        spaceItem.updateSpaceItem(spaceItemDTO);
        spaceItemRepository.save(spaceItem);
        };

    @Transactional
    public void deleteSpaceItem(Long spaceItemId) throws Exception {
        SpaceItem spaceItem = spaceItemRepository.findBySpaceItemIdAndIsDeletedFalse(spaceItemId)
                .orElseThrow(() -> new Exception("spaceItem id: " + spaceItemId + " 는 존재하지 않습니다"));
        spaceItem.setIsDeleted();
    }
}

