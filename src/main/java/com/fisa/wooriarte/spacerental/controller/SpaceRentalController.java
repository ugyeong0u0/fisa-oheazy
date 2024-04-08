package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class SpaceRentalController {
    private final SpaceRentalService spaceRentalService;

    @Autowired
    public SpaceRentalController(SpaceRentalService spaceRentalService) {
        this.spaceRentalService = spaceRentalService;
    }

    //공간대여자 회원가입
    @PostMapping("/space-rental")
    public String addSpaceRental(@RequestBody SpaceRentalDTO spaceRentalDTO) {
        if(spaceRentalService.addSpaceRental(spaceRentalDTO))
            return "success";
        return "fail";
    }

    //공간대여자 로그인
    @PostMapping("/space-rental/login")
    public String loginSpaceRental(@RequestBody Map<String, String> loginInfo) {
        String id = loginInfo.get("id");
        String pwd = loginInfo.get("pwd");
        if(spaceRentalService.loginSpaceRental(id, pwd))
            return "login success";
        return "login fail";
    }

    //공간대여자 정보 조회
    @GetMapping("/space-rental/{space_rental_id}")
    public String getSpaceRentalInfo(@PathVariable("space_rental_id") Long spaceRentalId) {
        SpaceRentalDTO spaceRentalDTO = spaceRentalService.findById(spaceRentalId);
        return spaceRentalDTO.toString();
    }

    //공간대여자 정보 갱신
    @PutMapping("/space-rental/{space_rental_id}")
    public String updateSpaceRentalInfo(@PathVariable("space_rental_id") Long spaceRentalId, @RequestBody SpaceRentalDTO spaceRentalDTO) {
        if(spaceRentalService.updateSpaceRental(spaceRentalId, spaceRentalDTO))
            return "success";
        return "fail";
    }

    //공간대여자 삭제(delete를 true로 변경)
    @DeleteMapping("/space-rental/{space_rental_id}")
    public String deleteSpaceRental(@PathVariable("space_rental_id") Long spaceRentalId) {
        if(spaceRentalService.deleteSpaceRental(spaceRentalId))
            return "success";
        return "fail";
    }
    /*
    예외 처리하는 부분
    DataIntegrityViolationException: 데이터베이스 제약조건, 무결성 위반 시도 (이미 존재하는 아이디 삽입, 이미 삭제된 사용자 삭제 등등)
    NoSuchElementException: 검색 요소가 발견되지 않음 (해당 아이디 찾을 수 없음 등등)
     */
    @ExceptionHandler({NoSuchElementException.class})
    public String handleNoSuchElementException(Exception e) {
        return "해당 id를 사용하는 유저가 없습니다";
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public String handleDataIntegrityViolationException(Exception e) {
        return "불가능한 요청입니다";
    }
}