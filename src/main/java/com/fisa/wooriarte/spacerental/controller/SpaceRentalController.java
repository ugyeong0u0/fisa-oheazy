package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/space-rental")
public class SpaceRentalController {
    @Autowired
    private SpaceRentalService service;

    //사용자 회원가입(사용자 추가)
    @PostMapping("")
    public String addSpaceRental(@RequestBody SpaceRentalDTO spaceRentalDTO) throws Exception {
        if(service.addSpaceRental(spaceRentalDTO))
            return "success";
        return "fail";
    }

    //사용자 정보를 요청
    @GetMapping("/{id}")
    public String getSpaceRentalInfo(@PathVariable("id") String id) throws Exception{
        try {
            SpaceRentalDTO spaceRentalDTO = service.findById(id);
            return spaceRentalDTO.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    //사용자 정보를 갱신
    @PatchMapping("{id}")
    public String updateSpaceRentalInfo(@PathVariable("id") String id, @RequestBody SpaceRentalDTO spaceRentalDTO) throws Exception {
        try {
            if(service.updateSpaceRental(id, spaceRentalDTO))
                return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    //사용자 삭제(delete를 true로 변경)
    @DeleteMapping("{id}")
    public String deleteSpaceRental(@PathVariable("id") String id) {
        try {
            if(service.deleteSpaceRental(id))
                return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    //매칭 조회 TBD
    @GetMapping("{id}/{matching_status}")
    public String getSpaceRentalMatching(@PathVariable("id") String id, @PathVariable("matching_status") int status) {
        // 나중에 matching 생기면 추가할 내용
        return null;
    }

    /*
    예외 처리하는 부분
    DataIntegrityViolationException: 데이터베이스 제약조건, 무결성 위반 시도 (이미 존재하는 아이디 삽입, 이미 삭제된 사용자 삭제 등등)
    NoSuchElementException: 검색 요소가 발견되지 않음 (해당 아이디 찾을 수 없음 등등)
     */
    @ExceptionHandler({DataIntegrityViolationException.class, NoSuchElementException.class})
    public void handle(Exception e) {
        System.err.println(e.getMessage());
    }
}