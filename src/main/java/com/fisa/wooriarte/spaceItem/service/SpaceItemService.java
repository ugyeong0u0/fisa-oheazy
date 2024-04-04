package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
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
    public SpaceItemService(SpaceItemRepository spaceItemRepository){
        this.spaceItemRepository = spaceItemRepository;
    }

    public boolean addSpaceItem(SpaceItemDTO spaceItemDTO){
        spaceItemRepository.save(spaceItemDTO.toEntity());
        return true;
    }

    public List<SpaceItemDTO> findAll() {
        return spaceItemRepository.findAll().stream()
                .map(SpaceItem::toDTO) // 람다식을 사용하여 각 SpaceItem 엔티티를 SpaceItemDTO로 변환
                .collect(Collectors.toList());
    }

    public Optional<SpaceItemDTO> findSpaceItembyId(Long spaceId) {
        return spaceItemRepository.findById(spaceId)
                .map(SpaceItem::toDTO);

    }

    public boolean updateSpaceItem(Long id, SpaceItemDTO spaceItemDTO) {
        // 기존 엔티티를 찾고, 있으면 업데이트
        return spaceItemRepository.findById(id).map(existingItem -> {
            // DTO에서 엔티티로 프로퍼티 복사, "id", "createdAt" 등 변경되면 안되는 필드는 제외
            BeanUtils.copyProperties(spaceItemDTO, existingItem, "id", "createdAt");
            // 변경된 엔티티 저장
            spaceItemRepository.save(existingItem);
            return true;
        }).orElse(false); // 찾지 못하면 false 반환
    }
}
