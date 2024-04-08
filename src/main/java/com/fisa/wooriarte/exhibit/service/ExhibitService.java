package com.fisa.wooriarte.exhibit.service;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.exhibit.dto.ExhibitDTO;
import com.fisa.wooriarte.exhibit.repository.ExhibitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    public ExhibitService(ExhibitRepository exhibitRepository){
        this.exhibitRepository = exhibitRepository;
    }

    //현재 진행 중인 전시데이터 목록 출력
    public List<ExhibitDTO> findAllExhibits() {
        System.out.println("findAllExhibits");
        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return exhibitRepository.findAll().stream()
                .filter(exhibit -> {
                    Date startDate = exhibit.getStartDate();
                    Date endDate = exhibit.getEndDate();
                    return startDate.before(todayDate) && endDate.after(todayDate) && !exhibit.isDeleted();
                })
                .map(ExhibitDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //전시 1개 출력
    public Optional<ExhibitDTO> findExhibitbyId(Long ExhibitId) {
        System.out.println("findExhibitItemById");
        return exhibitRepository.findById(ExhibitId)
                .map(ExhibitDTO::fromEntity);
    }

    //전시 생성
    public void addExhibit(ExhibitDTO exhibitDTO) {
//        Matching matching = matchingRepository.findById(matchingId)
//                .orElseThrow(() -> new IllegalArgumentException("Matching not found with id: " + matchingId));
        // ExhibitDTO에 사용자 정보 설정
//        ExhibitDTO.setMatichingId(matching.getMatchingId());
        //TicketDTO -> 엔티티 변환
        Exhibit exhibit = exhibitDTO.toEntity();
        //Ticket 엔티티 저장
        Exhibit savedExhibit = exhibitRepository.save(exhibit);
        //Ticket 엔티티 -> TicketDTO 변환
        ExhibitDTO.fromEntity(savedExhibit);
    }

    @Transactional
    public void updateExhibit(Long id, ExhibitDTO exhibitDTO) {
        Exhibit exhibit = exhibitRepository.findById(exhibitDTO.getExhibitId())
                .orElseThrow(() -> new NoSuchElementException("Fail to update. No one uses that ID"));

        // 변경 불가능한 프로퍼티를 무시할 이름 배열 생성
        String[] ignoreProperties = {"artistName", "hostName", "exhibitId", "soldAmount", "date"};

        // BeanUtils를 사용하여 DTO의 내용을 엔티티에 복사
        BeanUtils.copyProperties(exhibitDTO, exhibit, ignoreProperties);

        // 변경된 엔티티 저장
        exhibitRepository.save(exhibit);
    }

    //전시 삭제 여부 변경
    @Transactional
    public void deleteExhibitById(long exhibitId){
        //exhibit_id로 검색
        Optional<Exhibit> optionalExhibit = exhibitRepository.findById(exhibitId);

        //exhibit이 존재할 경우 deleted 컬럼 변경
        optionalExhibit.ifPresent(Exhibit::setDeleted);
    }


}
