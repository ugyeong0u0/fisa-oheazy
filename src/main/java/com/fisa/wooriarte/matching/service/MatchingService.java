package com.fisa.wooriarte.matching.service;

import com.fisa.wooriarte.matching.dto.MatchingDto;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
import com.fisa.wooriarte.matching.dto.MatchingResponseDto;
import com.fisa.wooriarte.matching.repository.MatchingRepository;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final ProjectItemRepository projectItemRepository;
    private final SpaceItemRepository spaceItemRepository;

    @Autowired
    public MatchingService(MatchingRepository matchingRepository, ProjectItemRepository projectItemRepository, SpaceItemRepository spaceItemRepository) {
        this.matchingRepository = matchingRepository;
        this.projectItemRepository = projectItemRepository;
        this.spaceItemRepository = spaceItemRepository;
    }

    /**
     * 모든 매칭 조회
     * @return 모든 매칭의 DTO 리스트
     */
    public List<MatchingDto> getAllMatching() {
        return matchingRepository.findAll()
                .stream()
                .map(MatchingDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 임대사업자가 매칭 신청
     * @param spaceItemId 공간 아이템 ID
     * @param projectItemId 프로젝트 아이템 ID
     * @return 생성된 매칭의 DTO
     */
    @Transactional
    public MatchingDto addMatchingBySpaceRental(Long spaceItemId, Long projectItemId) {
        SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
        ProjectItem projectItem = projectItemRepository.findById(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));

        if (matchingRepository.findByProjectItemAndSpaceItem(projectItem, spaceItem).isPresent()) {
            throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다.");
        }

        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
                .spaceItem(spaceItem)
                .projectItem(projectItem)
                .sender(spaceItem.getSpaceRental().getSpaceRentalId())
                .receiver(projectItem.getProjectManager().getProjectManagerId())
                .senderType(SenderType.SPACERENTAL)
                .build();

        matchingRepository.save(matching);
        return MatchingDto.fromEntity(matching);
    }

    /**
     * 프로젝트 매니저가 매칭 신청
     * @param projectItemId 프로젝트 아이템 ID
     * @param spaceItemId 공간 아이템 ID
     * @return 생성된 매칭의 DTO
     */
    @Transactional
    public MatchingDto addMatchingByProjectManager(Long projectItemId, Long spaceItemId) {
        SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 공간 아이템을 찾을 수 없습니다."));
        ProjectItem projectItem = projectItemRepository.findById(projectItemId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템을 찾을 수 없습니다."));

        if (matchingRepository.findByProjectItemAndSpaceItem(projectItem, spaceItem).isPresent()) {
            throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다.");
        }

        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
                .spaceItem(spaceItem)
                .projectItem(projectItem)
                .sender(projectItem.getProjectManager().getProjectManagerId())
                .receiver(spaceItem.getSpaceRental().getSpaceRentalId())
                .senderType(SenderType.PROJECTMANAGER)
                .build();

        matchingRepository.save(matching);
        return MatchingDto.fromEntity(matching);
    }

    /**
     * 특정 매칭을 조회
     * @param matchingId 매칭 ID
     * @return 매칭의 DTO
     */
    public MatchingDto getMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new NoSuchElementException("해당 매칭을 찾을 수 없습니다."));
        return MatchingDto.fromEntity(matching);
    }

    /**
     * 매칭 상태 변경
     * @param matchingId 매칭 ID
     * @param matchingStatus 변경할 매칭 상태
     * @return 상태 변경 성공 여부
     */
    @Transactional
    public boolean updateMatching(Long matchingId, MatchingStatus matchingStatus) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new NoSuchElementException("해당 매칭을 찾을 수 없습니다."));

        matching.setMatchingStatus(matchingStatus);
        matchingRepository.save(matching);
        return true;
    }

    /**
     * 임대사업자가 보낸 대기 중인 매칭 조회
     * @param spaceRentalId 공간 대여자 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findSpaceRentalWaitingMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(
                spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream()
                .map(MatchingResponseDto::fromEntityBySpaceRental)
                .collect(Collectors.toList());
    }

    /**
     * 임대사업자가 받은 대기 중인 매칭 조회
     * @param spaceRentalId 공간 대여자 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findSpaceRentalOfferMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(
                spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream()
                .map(MatchingResponseDto::fromEntityBySpaceRental)
                .collect(Collectors.toList());
    }

    /**
     * 임대사업자의 성사된 매칭을 조회
     * @param spaceRentalId 공간 대여자 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findSpaceRentalSuccessMatching(Long spaceRentalId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderSpaceRental(spaceRentalId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverSpaceRental(spaceRentalId);
        return Stream.concat(senderList.stream(), receiverList.stream())
                .map(MatchingResponseDto::fromEntityBySpaceRental)
                .collect(Collectors.toList());
    }

    /**
     * 프로젝트 매니저가 보낸 대기 중인 매칭 조회
     * @param projectManagerId 프로젝트 매니저 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findProjectManagerWaitingMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(
                projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream()
                .map(MatchingResponseDto::fromEntityByProjectManager)
                .collect(Collectors.toList());
    }

    /**
     * 프로젝트 매니저가 받은 대기 중인 매칭 조회
     * @param projectManagerId 프로젝트 매니저 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findProjectManagerOfferMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(
                projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream()
                .map(MatchingResponseDto::fromEntityByProjectManager)
                .collect(Collectors.toList());
    }

    /**
     * 프로젝트 매니저의 성사된 매칭 조회
     * @param projectManagerId 프로젝트 매니저 ID
     * @return 매칭 응답 DTO 리스트
     */
    public List<MatchingResponseDto> findProjectManagerSuccessMatching(Long projectManagerId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderProjectManager(projectManagerId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverProjectManager(projectManagerId);
        return Stream.concat(senderList.stream(), receiverList.stream())
                .map(MatchingResponseDto::fromEntityByProjectManager)
                .collect(Collectors.toList());
    }
}