package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean addSpaceItem(SpaceItemDTO spaceItemDTO){
        System.out.println("addSpaceItem");
        spaceItemRepository.save(spaceItemDTO.toEntity(spaceRentalRepository));
        return true;
    }

    public List<SpaceItemDTO> findAll() {
        System.out.println("findAll");
        return spaceItemRepository.findAll().stream()
                .map(SpaceItemDTO::fromEntity) // 람다식을 사용하여 각 SpaceItem 엔티티를 SpaceItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<SpaceItemDTO> findSpaceItembyId(Long spaceItemId) {
        System.out.println("findSpaceItemById");
        return spaceItemRepository.findById(spaceItemId)
                .map(SpaceItemDTO::fromEntity);

    }
    @Transactional
    public void updateSpaceItem(Long spaceItemId, SpaceItemDTO spaceItemDTO) {
        System.out.println("updateSpaceItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        spaceItemRepository.findById(spaceItemId).ifPresent(existingItem -> {
            // DTO에서 엔티티로 프로퍼티 복사, "id", "createdAt" 등 변경되면 안되는 필드는 제외
            BeanUtils.copyProperties(spaceItemDTO, existingItem, "id", "createdAt");
            // 변경된 엔티티 저장
            spaceItemRepository.save(existingItem);
        });
    }

    @Transactional
    public void deleteSpaceItem(Long spaceItemId) throws Exception {
        SpaceItem spaceItem = spaceItemRepository.findBySpaceItemIdAndIsDeletedFalse(spaceItemId)
                .orElseThrow(() -> new Exception("spaceItem id: " + spaceItemId + " 는 존재하지 않습니다"));
        spaceItem.updateIsDeleted();
    }

}
