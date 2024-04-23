package com.fisa.wooriarte.matching.service;

import com.fisa.wooriarte.matching.dto.MatchingDto;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
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
    public MatchingService(MatchingRepository matchingRepository,ProjectItemRepository projectItemRepository, SpaceItemRepository spaceItemRepository) {
        this.matchingRepository = matchingRepository;
        this.spaceItemRepository = spaceItemRepository;
        this.projectItemRepository = projectItemRepository;
    }

    // 모든 매칭 조회
    public List<MatchingDto> getAllMatching() {
        return matchingRepository.findAll().stream().map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    //공간 대여자가 매칭 신청
    @Transactional
    public MatchingDto addMatchingBySpaceRental(Long spaceItemId, Long projectItemId) {

        SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectItemId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        if(matchingRepository.findByProjectItemAndSpaceItem(projectItem, spaceItem).isPresent()) {
                throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다");
        }
        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
                .spaceItem(spaceItem)
                .projectItem(projectItem)
                .sender(spaceItem.getSpaceItemId())
                .receiver(projectItem.getProjectManager().getProjectManagerId())
                .senderType(SenderType.SPACERENTAL)
                .build();
        matchingRepository.save(matching);
        return MatchingDto.fromEntity(matching);
    }

    //프로젝트 매니저가 매칭 신청
    /**
     * 프로젝트 매니저가 작가가 선택한 공간과의 매칭 -
     *  - 작가가 보유하고 있는 프로젝트를 작가가 선택한 공간 id (=공간대여자의 item id)
     * @param projectItemId : 작가가 보유하고 있는 프로젝트 id
     * @param spaceItemId : 작가가 선택한 공간 id
     * @return 작가가 선택한 공간 id 와 공간대여자가 가지고 있는 item id 가 일치하여 해당 공간에 대한 객체가 반환
     */
    @Transactional
    public MatchingDto addMatchingByProjectManager(Long projectItemId, Long spaceItemId) {

        SpaceItem spaceItem = spaceItemRepository.findById(spaceItemId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectItemId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        // 신청하려는 작가 item 과 공간대여자의 item이 이미 매칭이 된 대상인가 (=매칭테이블에 있는가)
        if(matchingRepository.findByProjectItemAndSpaceItem(projectItem, spaceItem).isPresent()) {
            throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다");
        }

        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
                .spaceItem(spaceItem)
                .projectItem(projectItem)
                .sender(projectItem.getProjectItemId())
                .receiver(spaceItem.getSpaceRental().getSpaceRentalId())
                .senderType(SenderType.PROJECTMANAGER)
                .build();

        matchingRepository.save(matching);
        return MatchingDto.fromEntity(matching);
    }

    public MatchingDto getMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        return MatchingDto.fromEntity(matching);
    }

    // 매칭 진행 상태 변경
    public boolean updateMatching(Long matchingId, MatchingStatus matchingStatus) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        matching.setMatchingStatus(matchingStatus);
        matchingRepository.save(matching);
        return true;
    }

    // 공간 대여자가 보낸 매칭 조회
    public List<MatchingDto> findSpaceRentalWaitingMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    // 공간 대여자가 받은 매칭 조회
    public List<MatchingDto> findSpaceRentalOfferMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    // 공간 대여자의 성사된 매칭 조회
    public List<MatchingDto> findSpaceRentalSuccessMatching(Long spaceRentalId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderSpaceRental(spaceRentalId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverSpaceRental(spaceRentalId);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 보낸 매칭 조회
    public List<MatchingDto> findProjectManagerWaitingMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 받은 매칭
    public List<MatchingDto> findProjectManagerOfferMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(MatchingDto::fromEntity).collect(Collectors.toList());
    }

    //프로젝트 매니저의 성사된 매칭
    public List<MatchingDto> findProjectManagerSuccessMatching(Long projectManagerId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderProjectManager(projectManagerId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverProjectManager(projectManagerId);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(MatchingDto::fromEntity).collect(Collectors.toList());
    }
}
