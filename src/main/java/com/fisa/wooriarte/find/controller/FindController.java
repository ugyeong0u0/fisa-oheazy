package com.fisa.wooriarte.find.controller;

import com.fisa.wooriarte.find.dto.request.FindBusinessIdRequest;
import com.fisa.wooriarte.find.dto.request.FindBusinessPassRequest;
import com.fisa.wooriarte.find.service.FindService;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindController {
    @Autowired
    private FindService findService;

    // 공간렌탈 id찾기
    @PostMapping("/find-business-id")
    public ResponseEntity<?> findBusinessId(@RequestBody FindBusinessIdRequest businessDTO) {
        try {
            if (businessDTO.getCheck()) {
                SpaceRentalDTO spaceRentalDTO = findService.getBusinessId(businessDTO);
                return ResponseEntity.ok(spaceRentalDTO.getId());

            } else {
                return ResponseEntity.badRequest().body("인증번호 확인 안됨");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청 혹은 없음");
        }

    }

    // 공간 대여자 비밀번호 찾기
    @PostMapping("/find-business-pw")
    public ResponseEntity<?> findBusinessPass(@RequestBody FindBusinessPassRequest pwDTO) {
        try {
            if (pwDTO.getCheck()) {
                SpaceRentalDTO spaceRentalDTO = findService.getBusinessPw(pwDTO);
                return ResponseEntity.ok(spaceRentalDTO.getPwd());

            } else {
                return ResponseEntity.badRequest().body("인증번호 확인 안됨");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청 혹은 없음");
        }
    }

}
