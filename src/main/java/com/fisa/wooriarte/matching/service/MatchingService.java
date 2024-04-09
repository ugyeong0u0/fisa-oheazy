package com.fisa.wooriarte.matching.service;

import com.fisa.wooriarte.matching.DTO.MatchingDTO;
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
    public List<MatchingDTO> getAllMatching() {
        return matchingRepository.findAll().stream().map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    //공간 대여자가 매칭 신청
    @Transactional
    public MatchingDTO addMatchingBySpaceRental(Long spaceId, Long projectId) {
        if(matchingRepository.findByProjectIdAndSpaceId(projectId, spaceId).isPresent()) {
                throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다");
        }
        SpaceItem spaceItem = spaceItemRepository.findById(spaceId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
<<<<<<< HEAD
                .sender(spaceItem.getSpaceRental().getSpaceRentalId())
                .receiver(projectItem.getProjectManager().getProjectManagerId())
=======
                .sender(spaceItem.getSpaceItemId())
                .receiver(projectItem.getProjectItemId())
>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
                .spaceId(spaceId)
                .projectId(projectId)
                .senderType(SenderType.SPACERENTAL)
                .build();

        matchingRepository.save(matching);
        return MatchingDTO.fromEntity(matching);
    }

    //프로젝트 매니저가 매칭 신청
    @Transactional
    public MatchingDTO addMatchingByProjectManager(Long projectId, Long spaceId) {
        if(matchingRepository.findByProjectIdAndSpaceId(projectId, spaceId).isPresent()) {
            throw new DataIntegrityViolationException("해당 매칭이 이미 존재합니다");
        }
        SpaceItem spaceItem = spaceItemRepository.findById(spaceId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.REQUESTWAITING)
<<<<<<< HEAD
                .sender(projectItem.getProjectManager().getProjectManagerId())
                .receiver(spaceItem.getSpaceRental().getSpaceRentalId())
=======
                .sender(spaceItem.getSpaceItemId())
                .receiver(projectItem.getProjectItemId())
>>>>>>> 8d6934fd78add816b42eb102737c4fdbd7968a71
                .spaceId(spaceId)
                .projectId(projectId)
                .senderType(SenderType.PROJECTMANAGER)
                .build();

        matchingRepository.save(matching);
        return MatchingDTO.fromEntity(matching);
    }

    public MatchingDTO getMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        return MatchingDTO.fromEntity(matching);
    }

    // 매칭 진행 상태 변경
    public boolean updateMatching(Long matchingId, MatchingStatus matchingStatus) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        matching.setMatchingStatus(matchingStatus);
        matchingRepository.save(matching);
        return true;
    }

    // 공간 대여자가 보낸 매칭 조회
    public List<MatchingDTO> findSpaceRentalWaitingMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    // 공간 대여자가 받은 매칭 조회
    public List<MatchingDTO> findSpaceRentalOfferMatching(Long spaceRentalId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(spaceRentalId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    // 공간 대여자의 성사된 매칭 조회
    public List<MatchingDTO> findSpaceRentalSuccessMatching(Long spaceRentalId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderSpaceRental(spaceRentalId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverSpaceRental(spaceRentalId);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 보낸 매칭 조회
    public List<MatchingDTO> findProjectManagerWaitingMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 받은 매칭git
    public List<MatchingDTO> findProjectManagerOfferMatching(Long projectManagerId) {
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(projectManagerId, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }

    //프로젝트 매니저의 성사된 매칭
    public List<MatchingDTO> findProjectManagerSuccessMatching(Long projectManagerId) {
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderProjectManager(projectManagerId);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverProjectManager(projectManagerId);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(MatchingDTO::fromEntity).collect(Collectors.toList());
    }
}
