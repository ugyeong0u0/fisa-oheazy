package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
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

//    public boolean updateSpaceItem(Long id, SpaceItemDTO spaceItemDTO) {
//        // 기존 엔티티를 찾고, 있으면 업데이트
//        return spaceItemRepository.findById(id).map(existingItem -> {
//            SpaceItem updatedItem = spaceItemDTO.toEntity();
//            updatedItem.setId(existingItem.getId()); // ID를 유지
//            spaceItemRepository.save(updatedItem);
//            return true;
//        }).orElse(false); // 찾지 못하면 false 반환
//    }
}
