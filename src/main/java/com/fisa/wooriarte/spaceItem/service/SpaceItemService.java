package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
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

    @Autowired
    public SpaceItemService(SpaceItemRepository spaceItemRepository) {
        this.spaceItemRepository = spaceItemRepository;
    }

    @Transactional
    public boolean addSpaceItem(SpaceItemDTO spaceItemDTO){
        System.out.println("addSpaceItem");
        spaceItemRepository.save(spaceItemDTO.toEntity());
        return true;
    }

    public List<SpaceItemDTO> findAll() {
        System.out.println("findAll");
        return spaceItemRepository.findAll().stream()
                .map(SpaceItem::toDTO) // 람다식을 사용하여 각 SpaceItem 엔티티를 SpaceItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<SpaceItemDTO> findSpaceItembyId(Long spaceId) {
        System.out.println("findSpaceItemById");
        return spaceItemRepository.findById(spaceId)
                .map(SpaceItem::toDTO);

    }
    @Transactional
    public void updateSpaceItem(Long id, SpaceItemDTO spaceItemDTO) {
        System.out.println("updateSpaceItem");
        // 기존 엔티티를 찾고, 있으면 업데이트
        spaceItemRepository.findById(id).ifPresent(existingItem -> {
            // DTO에서 엔티티로 프로퍼티 복사, "id", "createdAt" 등 변경되면 안되는 필드는 제외
            BeanUtils.copyProperties(spaceItemDTO, existingItem, "id", "createdAt");
            // 변경된 엔티티 저장
            spaceItemRepository.save(existingItem);
        });
    }

    @Transactional
    public void deleteSpaceItem(Long id) throws Exception {
        SpaceItem spaceItem = spaceItemRepository.findBySpaceIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new Exception("spaceItem id: " + id + " 는 존재하지 않습니다"));
        spaceItem.updateIsDeleted();
    }

}
