package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        // 공간대여자가 있을 경우에 생성
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceItemDTO.getSpaceRentalId())
                .orElseThrow(() -> new NoSuchElementException("No space Rental"));
        spaceItemRepository.save(spaceItemDTO.toEntity(spaceRental));
        return true;
    }

    public List<SpaceItemDto> findAll() {
        System.out.println("findAll");
        List<SpaceItem> spaceItemList = spaceItemRepository.findAllByIsDeletedFalse()
                .orElseThrow(() -> new NoSuchElementException("No Space Item"));
        return spaceItemList.stream()
                .map(SpaceItemDto::fromEntity) // 람다식을 사용하여 각 SpaceItem 엔티티를 SpaceItemDTO로 변환
                .collect(Collectors.toList());
    }


    /**
     *
     * @param spaceItemId
     * @return
     */
    public SpaceItemDto findSpaceItemById(Long spaceItemId) {
        System.out.println("findSpaceItemById");
        SpaceItem spaceItem = spaceItemRepository.findBySpaceItemIdAndIsDeletedFalse(spaceItemId).orElseThrow(() -> new NoSuchElementException("No Space Item"));
        return SpaceItemDto.fromEntity(spaceItem);
    }

    public List<SpaceItemDto> findBySpaceRentalId(Long spaceRentalId) {
        // Optional<List<ProjectItem>>에서 List<ProjectItem>을 얻기 위해 orElseGet을 사용합니다.
        // Optional이 비어있다면, 빈 리스트를 반환합니다.
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId).orElseThrow(() -> new NoSuchElementException("No Project Manager"));
        List<SpaceItem> spaceItems = spaceItemRepository.findBySpaceRental(spaceRental)
                .orElse(Collections.emptyList());

        // Stream을 사용하여 각 ProjectItem을 ProjectItemDTO로 변환합니다.
        return spaceItems.stream()
                .map(SpaceItemDto::fromEntity)
                .collect(Collectors.toList());
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

