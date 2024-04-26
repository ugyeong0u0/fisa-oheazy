package com.fisa.wooriarte.admin.controller;

import com.fisa.wooriarte.admin.dto.AdminDto;
import com.fisa.wooriarte.admin.service.AdminService;
import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectItem.service.ProjectItemService;
import com.fisa.wooriarte.projectmanager.controller.ProjectManagerController;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import com.fisa.wooriarte.user.dto.request.UserLoginRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ProjectItemService projectItemService;
    private final SpaceItemService spaceItemService;
    private final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);

    @Autowired
    public AdminController(AdminService adminService, ProjectItemService projectItemService, SpaceItemService spaceItemService) {
        this.adminService = adminService;
        this.projectItemService = projectItemService;
        this.spaceItemService = spaceItemService;
    }

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestBody AdminDto adminDto) {
        try {
            log.info("Adding a new admin");
            adminService.addAdmin(adminDto);
            return ResponseEntity.ok().body("Admin added successfully");
        } catch (Exception e) {
            log.error("Exception occurred while adding an admin: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add admin");
        }
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

    @GetMapping("/manage-item-approval")
    public ResponseEntity<?> manageItemApproval() {
        try {
            log.info("Managing item approval");
            List<ProjectItemDto> projectItemList = projectItemService.getUnapprovedItems();
            List<SpaceItemDto> spaceItemList = spaceItemService.getUnapprovedItems();

            Map<String, Object> response = new HashMap<>();
            response.put("projectItems", projectItemList);
            response.put("spaceItems", spaceItemList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception occurred while managing item approval: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to manage item approval");
        }
    }

    @PostMapping("/approve-project-item/{project-item-id}")
    public ResponseEntity<?> projectItemApproval(@PathVariable("project-item-id") Long projectItemId) {
        try {
            log.info("Approving project item with id: {}", projectItemId);
            projectItemService.approveItem(projectItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Exception occurred while approving project item: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve project item");
        }
    }

    @PostMapping("/approve-space-item/{space-item-id}")
    public ResponseEntity<?> spaceItemApproval(@PathVariable("space-item-id") Long spaceItemId) {
        try {
            log.info("Approving space item with ID: {}", spaceItemId);
            spaceItemService.approveItem(spaceItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Exception occurred while approving space item with ID: {}: ", spaceItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve space item");
        }
    }

    @PostMapping("/refuse-project-item/{project-item-id}")
    public ResponseEntity<?> refuseProjectItem(@PathVariable("project-item-id") Long projectItemId) {
        try {
            log.info("Refusing project item with ID: {}", projectItemId);
            projectItemService.refuseItem(projectItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Exception occurred while refusing project item with ID: {}: ", projectItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to refuse project item");
        }
    }

    @PostMapping("/refuse-space-item/{space-item-id}")
    public ResponseEntity<?> refuseSpaceItem(@PathVariable("space-item-id") Long spaceItemId) {
        try {
            log.info("Refusing space item with ID: {}", spaceItemId);
            spaceItemService.refuseItem(spaceItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Exception occurred while refusing space item with ID: {}: ", spaceItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to refuse space item");
        }
    }
}


