package com.fisa.wooriarte.projectmanager.controller;

import com.fisa.wooriarte.projectmanager.DTO.ProjectManagerDTO;
import com.fisa.wooriarte.projectmanager.service.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/project-mangers")
public class ProjectManagerController {

    @Autowired
    ProjectManagerService service;

    //프로젝트 매니저 회원가입
    @PostMapping("")
    public String addProjectManager(@RequestBody ProjectManagerDTO projectManagerDTO) {
        if(service.addProjcerManager(projectManagerDTO))
            return "success";
        return "fail";
    }

    //프로젝트 매니저 로그인
    @PostMapping("/login")
    public String loginProjectManager(@RequestBody Map<String, String> loginInfo) {
        String id = loginInfo.get("id");
        String pwd = loginInfo.get("pwd");
        if(service.loginProjectManager(id, pwd))
            return "login success";
        return "login fail";
    }

    //프로젝트 매니저 정보 조회
    @GetMapping("/{id}")
    public String getProjectManagerInfo(@PathVariable("id") String id) {
        ProjectManagerDTO projectManagerDTO = service.findById(id);
        return projectManagerDTO.toString();
    }

    //프로젝트 매니저 정보 갱신
    @PatchMapping("{id}")
    public String updateProjectManagerInfo(@PathVariable("id") String id, @RequestBody ProjectManagerDTO projectManagerDTO) {
        if(service.updateProjectManager(id, projectManagerDTO))
            return "success";
        return "fail";
    }

    //프로젝트 매니저 삭제(delete를 true로 변경)
    @DeleteMapping("{id}")
    public String deleteProjectManager(@PathVariable("id") String id) {
        if(service.deleteProjectManager(id))
            return "success";
        return "fail";
    }

    //매칭 조회 TBD
    @GetMapping("{id}/{matching_status}")
    public String getProjectManagerMatching(@PathVariable("id") String id, @PathVariable("matching_status") int status) {
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
