package com.fisa.wooriarte.projectItem.controller;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDto;
import com.fisa.wooriarte.projectItem.dto.ProjectItemResponseDto;
import com.fisa.wooriarte.projectItem.service.ProjectItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RequestMapping("/api/project-items")
@RestController
public class ProjectItemController {

    private static final Logger log = LoggerFactory.getLogger(ProjectItemController.class);

    private final ProjectItemService projectItemService;

    @Autowired
    public ProjectItemController(ProjectItemService projectItemService) {
        this.projectItemService = projectItemService;
    }

    @GetMapping("/approved-all")
    public ResponseEntity<?> getAllApprovedProjectItemInfo() {
        try {
            List<ProjectItemResponseDto> items = projectItemService.findApprovedAll();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Failed to retrieve all project items", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to retrieve project item information."));
        }
    }

    @GetMapping("/unapproved-all")
    public ResponseEntity<?> getAllUnapprovedProjectItemInfo() {
        try {
            List<ProjectItemResponseDto> items = projectItemService.findUnapprovedAll();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Failed to retrieve all project items", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to retrieve project item information."));
        }
    }

    @GetMapping("/project-manager/{project-manager-id}")
    public ResponseEntity<List<ProjectItemResponseDto>> getProjectItemByProjectManagerId(@PathVariable("project-manager-id") Long projectManagerId) {
        List<ProjectItemResponseDto> projectItemDtoList = projectItemService.findByProjectManagerId(projectManagerId);
        return ResponseEntity.ok(projectItemDtoList);
    }


    @PostMapping("")
    public ResponseEntity<?> addProjectItem(@RequestBody ProjectItemDto projectItemDTO) {
        try {
            Long projectItemId = projectItemService.addProjectItem(projectItemDTO);
            return ResponseEntity.ok(Map.of("projectItemId", projectItemId));
        } catch (Exception e) {
            log.error("Failed to add project item", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add project item."));
        }
    }

    @GetMapping("/{project-item-id}")
    public ResponseEntity<?> getProjectItemInfo(@PathVariable(name = "project-item-id") Long projectItemId) {
        try {
            ProjectItemDto projectItemDto = projectItemService.findByProjectItemId(projectItemId);
            return ResponseEntity.ok(projectItemDto);
        } catch (NoSuchElementException e) {
            log.error("Failed to retrieve project item with id: {}", projectItemId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while retrieving project item with id: {}", projectItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to retrieve project item details."));
        }
    }

    @PutMapping("/{project-item-id}")
    public ResponseEntity<?> updateProjectItem(@PathVariable(name = "project-item-id") Long projectItemId, @RequestBody ProjectItemDto projectItemDTO) {
        try {
            projectItemService.updateProjectItem(projectItemId, projectItemDTO);
            return ResponseEntity.ok(Map.of("message", "Project item successfully updated."));
        } catch (Exception e) {
            log.error("Failed to update project item with id: {}", projectItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to update project item."));
        }
    }

    @DeleteMapping("/{project-item-id}")
    public ResponseEntity<?> deleteProjectItem(@PathVariable(name = "project-item-id") Long projectItemId) {
        try {
            projectItemService.deleteProjectItem(projectItemId);
            return ResponseEntity.ok(Map.of("message", "Project item successfully deleted."));
        } catch (Exception e) {
            log.error("Failed to delete project item with id: {}", projectItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to delete project item."));
        }
    }

    @GetMapping("/{start-date}/{end-date}/{city}")
    public ResponseEntity<?> getProjectItemByFilter(@PathVariable("start-date") LocalDate startDate, @PathVariable("end-date") LocalDate endDate, @PathVariable("city") String city) {
        try {
            List<ProjectItemResponseDto> projectItemDtoList = projectItemService.findByFilter(startDate, endDate, city);
            return ResponseEntity.ok(projectItemDtoList);
        } catch (Exception e) {
            log.error("Failed to find project item by filter", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to find project item"));
        }
    }
}
