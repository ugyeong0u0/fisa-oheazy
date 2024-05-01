package com.fisa.wooriarte.exhibit.service;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.exhibit.dto.ExhibitDto;
import com.fisa.wooriarte.exhibit.dto.ExhibitResponseDto;
import com.fisa.wooriarte.exhibit.repository.ExhibitRepository;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.matching.repository.MatchingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExhibitService {

    private final ExhibitRepository exhibitRepository;
    private final MatchingRepository matchingRepository;

    @Autowired
    public ExhibitService(ExhibitRepository exhibitRepository, MatchingRepository matchingRepository){

        this.exhibitRepository = exhibitRepository;
        this.matchingRepository = matchingRepository;
    }


    /**
     * 1. 현재 진행 중인 전시데이터 목록 출력
     * @return
     */
    public List<ExhibitResponseDto> findAllExhibits() {
        System.out.println("findAllExhibits");
        //오늘 날짜를 받아옴
        LocalDate today = LocalDate.now();

        //오늘 날짜와 전시 시작날짜, 전시 끝 날짜를 비교해서 사이 값만 출력
        return exhibitRepository.findAll().stream()
                .filter(exhibit -> {
                    LocalDate startDate = exhibit.getStartDate();
                    LocalDate endDate = exhibit.getEndDate();
                    return startDate.isBefore(today) && endDate.isAfter(today) && !exhibit.getIsDeleted();
                })
                .map(ExhibitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 2. 전시 1개 출력
     * @param ExhibitId
     * @return
     */
    public Optional<ExhibitDto> findExhibitById(Long ExhibitId) {

        System.out.println("findExhibitItemById");
        return exhibitRepository.findById(ExhibitId)
                .map(ExhibitDto::fromEntity);
    }

    /**
     * 3. 전시 생성
     * @param exhibitDTO : 입력된 전시 데이터
     * @param matchingId : 성사된 matchingId
     */
    public void addExhibit(ExhibitDto exhibitDTO, Long matchingId) {

        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("Matching not found with id: " + matchingId));
        // ExhibitDTO에 사용자 정보 설정
//        exhibitDTO.setMatchingId(matching.getMatchingId());
        //TicketDTO -> 엔티티 변환
        Exhibit exhibit = exhibitDTO.toEntity(matchingRepository);
        //Ticket 엔티티 저장
        Exhibit savedExhibit = exhibitRepository.save(exhibit);
        //Ticket 엔티티 -> TicketDTO 변환
        ExhibitDto.fromEntity(savedExhibit);
    }

    /**
     * 4. 전시 정보 수정
     * @param exhibitId : 수정할 exhibitId
     * @param exhibitDTO : 수정할 전시 데이터
     */
    @Transactional
    public void updateExhibit(Long exhibitId, ExhibitDto exhibitDTO) {
        Exhibit exhibit = exhibitRepository.findById(exhibitId)
                .orElseThrow(() -> new NoSuchElementException("Fail to update. No one uses that ID"));
        exhibit.updateExhibit(exhibitDTO);
        exhibitRepository.save(exhibit);
    }

    /**
     * 5. 전시 삭제 여부 변경
     * @param exhibitId
     */
    @Transactional
    public void deleteExhibitById(long exhibitId){
        //exhibit_id로 검색
        Exhibit exhibit = exhibitRepository.findById(exhibitId)
                .orElseThrow(() -> new NoSuchElementException("Fail to delete. No one uses that ID"));
        exhibit.setIsDeleted();
        exhibitRepository.save(exhibit);
    }


}
