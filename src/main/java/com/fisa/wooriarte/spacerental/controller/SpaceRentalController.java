package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/space-rental")
public class SpaceRentalController {
    private final SpaceRentalService spaceRentalService;

    @Autowired
    public SpaceRentalController(SpaceRentalService spaceRentalService) {
        this.spaceRentalService = spaceRentalService;
    }

    //사용자 회원가입(사용자 추가)
    @PostMapping("")
    public String addSpaceRental(@RequestBody SpaceRentalDTO spaceRentalDTO) {
        if(spaceRentalService.addSpaceRental(spaceRentalDTO))
            return "success";
        return "fail";
    }

    @PostMapping("/login")
    public String loginSpaceRental(@RequestBody Map<String, String> loginInfo) {
        String id = loginInfo.get("id");
        String pwd = loginInfo.get("pwd");
        if(spaceRentalService.loginSpaceRental(id, pwd))
            return "login success";
        return "login fail";
    }

    //사용자 정보를 요청
    @GetMapping("/{id}")
    public String getSpaceRentalInfo(@PathVariable("id") String id) {
        SpaceRentalDTO spaceRentalDTO = spaceRentalService.findById(id);
        return spaceRentalDTO.toString();
    }

    //사용자 정보를 갱신
    @PatchMapping("{id}")
    public String updateSpaceRentalInfo(@PathVariable("id") String id, @RequestBody SpaceRentalDTO spaceRentalDTO) {
        if(spaceRentalService.updateSpaceRental(id, spaceRentalDTO))
            return "success";
        return "fail";
    }

    //사용자 삭제(delete를 true로 변경)
    @DeleteMapping("{id}")
    public String deleteSpaceRental(@PathVariable("id") String id) {
        if(spaceRentalService.deleteSpaceRental(id))
            return "success";
        return "fail";
    }

    //매칭 조회 TBD
    @GetMapping("{id}/{matching_status}")
    public String getSpaceRentalMatching(@PathVariable("id") String id, @PathVariable("matching_status") int status) {
        // 나중에 matching 생기면 추가할 내용
        return "";
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