package com.fisa.wooriarte.spacerental.service;

import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SpaceRentalService {
    private final SpaceRentalRepository spaceRentalRepository;

    @Autowired
    public SpaceRentalService(SpaceRentalRepository spaceRentalRepository) {
        this.spaceRentalRepository = spaceRentalRepository;
    }
    /*
    사용자 추가
    1. 같은 아이디 사용 확인
        발견시 예외 처리
    2. DB에 저장
     */
    @Transactional
    public boolean addSpaceRental(SpaceRentalDTO sr) {
        Optional<SpaceRental> optionalSpaceRental = spaceRentalRepository.findBySpaceRentalId(sr.getId());
        if (optionalSpaceRental.isPresent()) {
            throw new DataIntegrityViolationException("Duplicate User id");
        }
        spaceRentalRepository.save(sr.toEntity());
        return true;
    }

    public boolean loginSpaceRental(String id, String pwd) {
        Optional<SpaceRental> optionalSpaceRental = spaceRentalRepository.findBySpaceRentalId(id);
        return optionalSpaceRental.isPresent() && optionalSpaceRental.get().getPwd().equals(pwd);
    }

    /*
    사용자 정보 검색
    1. id로 유저 검색
        없으면 예외 처리
    2. DTO로 변환 후 반환
     */
    public SpaceRentalDTO findById(String id) {
        SpaceRental spaceRental = spaceRentalRepository.findBySpaceRentalId(id)
                    .orElseThrow(() -> new NoSuchElementException("Fail to search info. No one uses that ID"));
        return spaceRental.toDTO();
    }

    /*
    사용자 정보 갱신
    1. id로 유저 검색
        없으면 예외처리
    2. BeanUtils.copyProperties(S, D, ignore)로 같은 컬럼 데이터 갱신
        S:source 복사할 대상(getter 필요)
        D:Destination 내용 저장할 대상(setter 필요)
        ignore: 제외할 내용 선택
            createAt: 생성 시점은 갱신하지 않음
            businessId: 고유 번호는 그대로 유지
     */
    @Transactional
    public boolean updateSpaceRental(String id, SpaceRentalDTO spaceRentalDTO) {
        SpaceRental spaceRental = spaceRentalRepository.findBySpaceRentalId(id)
                .orElseThrow(() -> new NoSuchElementException("Fail to update. No one uses that ID"));
        BeanUtils.copyProperties(spaceRentalDTO, spaceRental, "createAt", "businessId");
        spaceRentalRepository.save(spaceRental);
        return true;
    }

    /*
    사용자 삭제 soft-delete
    1. id로 유저 검색
        없으면 예외처리
    2. delete를 true로 변경
        이미 변경했으면 예외처리
     */
    @Transactional
    public boolean deleteSpaceRental(String id) {
        SpaceRental spaceRental = spaceRentalRepository.findBySpaceRentalId(id)
                .orElseThrow(() -> new NoSuchElementException("Fail to delete. No one uses that ID"));
        if(spaceRental.isDeleted()) {
            throw new DataIntegrityViolationException("Already deleted User");
        }
        spaceRental.setDeleted(true);
        spaceRentalRepository.save(spaceRental);
        return true;
    }
}