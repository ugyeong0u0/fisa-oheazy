package com.fisa.wooriarte.admin.controller;

import com.fisa.wooriarte.admin.dto.AdminDto;
import com.fisa.wooriarte.admin.service.AdminService;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.projectmanager.controller.ProjectManagerController;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;

    }

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDto adminDto) {

        adminService.addAdmin(adminDto);
        return ResponseEntity.ok().body("Admin added");
    }

    @PostMapping("/jwtlogin")
    public JwtToken login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            log.info("Trying to login with JWT");
            String id = userLoginRequestDto.getId();
            String pwd = userLoginRequestDto.getPwd();
            return adminService.login(id, pwd);
        } catch (Exception e) {
            log.error("Exception occurred while logging in with JWT: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed due to an exception.");
        }
    }

}


