package com.fisa.wooriarte.spaceItem.service;

import com.fisa.wooriarte.spaceItem.domain.City;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemResponseDto;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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

    /**
     * 공간 아이템 추가
     * @param spaceItemDTO 추가할 공간 아이템 정보
     * @return 추가된 공간 아이템 ID
     */
    @Transactional
    public Long addSpaceItem(SpaceItemDto spaceItemDTO) {
        try {
            SpaceRental spaceRental = spaceRentalRepository.findById(spaceItemDTO.getSpaceRentalId())
                    .orElseThrow(() -> new NoSuchElementException("해당 공간 대여 정보를 찾을 수 없습니다."));
            SpaceItem spaceItem = spaceItemDTO.toEntity(spaceRental);
            spaceItemRepository.save(spaceItem);
            return spaceItem.getSpaceItemId();
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("공간 아이템 추가 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 승인된 모든 공간 아이템 조회
     * @return 승인된 공간 아이템 리스트
     */
    public List<SpaceItemResponseDto> findApprovedAll() {
        return spaceItemRepository.findAllByIsDeletedFalseAndApprovalTrue()
                .orElseThrow(() -> new NoSuchElementException("승인된 공간 아이템이 없습니다."))
                .stream()
                .map(SpaceItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 승인되지 않은 모든 공간 아이템 조회
     * @return 승인되지 않은 공간 아이템 리스트
     */
    public List<SpaceItemResponseDto> findUnapprovedAll() {
        return spaceItemRepository.findAllByIsDeletedFalseAndApprovalFalse()
                .orElseThrow(() -> new NoSuchElementException("승인되지 않은 공간 아이템이 없습니다."))
                .stream()
                .map(SpaceItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID의 공간 아이템 조회
     * @param spaceItemId 조회할 공간 아이템 ID
     * @return 조회된 공간 아이템 정보
     */
    public SpaceItemDto findSpaceItemById(Long spaceItemId) {
        SpaceItem spaceItem = spaceItemRepository.findBySpaceItemIdAndIsDeletedFalse(spaceItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
        return SpaceItemDto.fromEntity(spaceItem);
    }

    /**
     * 특정 임대사업자 ID의 공간 아이템 조회
     * @param spaceRentalId 임대사업자 ID
     * @return 해당 공간 대여에 속한 공간 아이템 리스트
     */
    public List<SpaceItemResponseDto> findBySpaceRentalId(Long spaceRentalId) {
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 공간 대여 정보를 찾을 수 없습니다."));
        List<SpaceItem> spaceItems = spaceItemRepository.findBySpaceRentalAndIsDeletedFalseAndApprovalTrue(spaceRental)
                .orElse(Collections.emptyList());

        return spaceItems.stream()
                .map(SpaceItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 공간 아이템 정보 수정
     * @param spaceItemId 수정할 공간 아이템 ID
     * @param spaceItemDTO 수정할 공간 아이템 정보
     */
    @Transactional
    public void updateSpaceItem(Long spaceItemId, SpaceItemDto spaceItemDTO) {
        try {
            SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId)
                    .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
            spaceItem.updateSpaceItem(spaceItemDTO);
            spaceItemRepository.save(spaceItem);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("공간 아이템 수정 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 공간 아이템 삭제.
     * @param spaceItemId 삭제할 공간 아이템 ID
     */
    @Transactional
    public void deleteSpaceItem(Long spaceItemId) {
        try {
            SpaceItem spaceItem = spaceItemRepository.findBySpaceItemIdAndIsDeletedFalse(spaceItemId)
                    .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
            spaceItem.setIsDeleted();
            spaceItemRepository.save(spaceItem);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("공간 아이템 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 필터 조건으로 공간 아이템 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param city 도시 이름
     * @return 조건에 맞는 공간 아이템 리스트
     */
    public List<SpaceItemResponseDto> findByFilter(LocalDate startDate, LocalDate endDate, String city) {
        List<SpaceItem> spaceItems = spaceItemRepository.findByEndDateGreaterThanEqualAndStartDateLessThanEqualAndCityAndIsDeletedFalseAndApprovalTrue(startDate, endDate, City.valueOf(city))
                .orElse(Collections.emptyList());
        return spaceItems.stream().map(SpaceItemResponseDto::fromEntity).collect(Collectors.toList());
    }

    /**
     * 공간 아이템 승인 처리
     * @param spaceItemId 승인할 공간 아이템 ID
     * @return 승인 성공 여부
     */
    public boolean approveItem(Long spaceItemId) {
        try {
            SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId)
                    .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
            spaceItem.setApproval();
            spaceItemRepository.save(spaceItem);
            return true;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("공간 아이템 승인 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 공간 아이템 거절 처리
     * @param spaceItemId 거절할 공간 아이템 ID
     * @return 거절 성공 여부
     */
    public boolean refuseItem(Long spaceItemId) {
        try {
            spaceItemRepository.deleteById(spaceItemId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("공간 아이템 거절 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 승인되지 않은 모든 공간 아이템 조회
     * @return 승인되지 않은 공간 아이템 리스트
     */
    public List<SpaceItemDto> getUnapprovedItems() {
        List<SpaceItem> spaceItems = spaceItemRepository.findAllByApprovalFalseAndIsDeletedFalse()
                .orElseThrow(() -> new NoSuchElementException("승인되지 않은 공간 아이템이 없습니다."));
        return spaceItems.stream()
                .map(SpaceItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}