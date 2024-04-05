package com.fisa.wooriarte.matching.service;

import com.fisa.wooriarte.matching.DTO.MatchingDTO;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.domain.MatchingStatus;
import com.fisa.wooriarte.matching.domain.SenderType;
import com.fisa.wooriarte.matching.repository.MatchingRepository;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import org.springframework.beans.BeanUtils;
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
    private final ProjectManagerRepository projectManagerRepository;
    private final SpaceRentalRepository spaceRentalRepository;


    @Autowired
    public MatchingService(MatchingRepository matchingRepository,ProjectItemRepository projectItemRepository, SpaceItemRepository spaceItemRepository,
    ProjectManagerRepository projectManagerRepository, SpaceRentalRepository spaceRentalRepository) {
        this.matchingRepository = matchingRepository;
        this.spaceItemRepository = spaceItemRepository;
        this.projectItemRepository = projectItemRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.spaceRentalRepository = spaceRentalRepository;
    }

    // 모든 매칭 조회
    public List<MatchingDTO> getAllMatching() {
        return matchingRepository.findAll().stream().map(Matching::toDTO).collect(Collectors.toList());
    }



    //공간 대여자가 매칭 신청
    @Transactional
    public MatchingDTO addMatchingBySpaceRental(Long spaceId, Long projectId) {
        Matching Matching = matchingRepository.findByProjectIdAndSpaceId(projectId, spaceId)
                .orElseThrow(() -> new DataIntegrityViolationException("해당 매칭이 이미 존재합니다"));

        SpaceItem spaceItem = spaceItemRepository.findById(spaceId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        Matching matching = new Matching();
        matching.setSender(spaceItem.getBusinessId());
        matching.setReceiver(projectItem.getBusinessId());
        matching.setMatchingStatus(MatchingStatus.WAITING);
        matching.setSpaceId(spaceId);
        matching.setProjectId(spaceId);
        matching.setSenderType(SenderType.SPACERENTAL);
        matchingRepository.save(matching);
        return matching.toDTO();
    }

    //프로젝트 매니저가 매칭 신청
    @Transactional
    public MatchingDTO addMatchingByProjectManager(Long projectId, Long spaceId) {
        Matching Matching = matchingRepository.findByProjectIdAndSpaceId(projectId, spaceId)
                .orElseThrow(() -> new DataIntegrityViolationException("해당 매칭이 이미 존재합니다"));

        SpaceItem spaceItem = spaceItemRepository.findById(spaceId).orElseThrow(() -> new NoSuchElementException("해당 공간 아이템 없음"));
        ProjectItem projectItem = projectItemRepository.findById(projectId).orElseThrow(() -> new NoSuchElementException("해당 프로젝트 아이템 없음"));

        Matching matching = new Matching();
        matching.setSender(projectItem.getBusinessId());
        matching.setReceiver(spaceItem.getBusinessId());
        matching.setMatchingStatus(MatchingStatus.WAITING);
        matching.setSpaceId(spaceId);
        matching.setProjectId(projectId);
        matching.setSenderType(SenderType.PROJECTMANAGER);
        matchingRepository.save(matching);
        return matching.toDTO();
    }

    public MatchingDTO getMatching(Long id) {
        Matching matching = matchingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        return matching.toDTO();
    }

    //매칭 갱신
    public boolean updateMatching(Long id, MatchingDTO matchingDTO) {
        Matching matching = matchingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        BeanUtils.copyProperties(matchingDTO, matching, "createAt", "matchingId");
        matchingRepository.save(matching);
        return true;
    }

    // 매칭 진행 상태 변경
    public boolean approvalMatching(Long id, MatchingStatus matchingStatus) {
        Matching matching = matchingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 매칭 없음"));
        matching.setMatchingStatus(matchingStatus);
        matchingRepository.save(matching);
        return true;
    }

    // 공간 대여자가 보낸 매칭 조회
    public List<MatchingDTO> findSpaceRentalWaitingMatching(String spaceRentalId) {
        Long id = spaceRentalRepository.findBySpaceRentalId(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getSpaceRentalId();
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(id, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(Matching::toDTO).collect(Collectors.toList());
    }

    // 공간 대여자가 받은 매칭 조회
    public List<MatchingDTO> findSpaceRentalOfferMatching(String spaceRentalId) {
        Long id = spaceRentalRepository.findBySpaceRentalId(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getSpaceRentalId();
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(id, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(Matching::toDTO).collect(Collectors.toList());
    }

    // 공간 대여자의 성사된 매칭 조회
    public List<MatchingDTO> findSpaceRentalSuccessMatching(String spaceRentalId) {
        Long id = spaceRentalRepository.findBySpaceRentalId(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getSpaceRentalId();
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderSpaceRental(id);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverSpaceRental(id);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(Matching::toDTO).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 보낸 매칭 조회
    public List<MatchingDTO> findProjectManagerWaitingMatching(String projectManagerId) {
        Long id = projectManagerRepository.findByProjectManagerId(projectManagerId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getProjectManagerId();
        List<Matching> list = matchingRepository.findBySenderAndMatchingStatusAndSenderType(id, MatchingStatus.REQUESTWAITING, SenderType.PROJECTMANAGER);
        return list.stream().map(Matching::toDTO).collect(Collectors.toList());
    }

    // 프로젝트 매니저가 받은 매칭
    public List<MatchingDTO> findProjectManagerOfferMatching(String projectManagerId) {
        Long id = projectManagerRepository.findByProjectManagerId(projectManagerId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getProjectManagerId();
        List<Matching> list = matchingRepository.findByReceiverAndMatchingStatusAndSenderType(id, MatchingStatus.REQUESTWAITING, SenderType.SPACERENTAL);
        return list.stream().map(Matching::toDTO).collect(Collectors.toList());
    }

    //프로젝트 매니저의 성사된 매칭
    public List<MatchingDTO> findProjectManagerSuccessMatching(String projectManagerId) {
        Long id = projectManagerRepository.findByProjectManagerId(projectManagerId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 없음")).getProjectManagerId();
        List<Matching> senderList = matchingRepository.findSuccessMatchingSenderProjectManager(id);
        List<Matching> receiverList = matchingRepository.findSuccessMatchingReceiverProjectManager(id);
        return Stream.concat(senderList.stream(), receiverList.stream()).map(Matching::toDTO).collect(Collectors.toList());
    }
}
