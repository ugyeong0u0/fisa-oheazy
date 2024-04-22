package com.fisa.wooriarte.projectItem.controller;

import com.fisa.wooriarte.projectItem.dto.ProjectItemDTO;
import com.fisa.wooriarte.projectItem.service.ProjectItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ProjectItemController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectItemController.class);

    private final ProjectItemService projectItemService;

    @Autowired
    public ProjectItemController(ProjectItemService projectItemService) {
        this.projectItemService = projectItemService;
    }

    // 모든 프로젝트 아이템 정보 조회
    @GetMapping("/project-item")
    public List<ProjectItemDTO> getAllProjectItemInfo() throws Exception {
        try {
            return projectItemService.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all project items", e);
            throw new Exception("프로젝트 아이템 정보를 가져오지 못했습니다.");
        }
    }

    // 프로젝트 아이템 추가
    @PostMapping("/project-item")
    public String addProjectItem(@RequestBody ProjectItemDTO projectItemDTO) {
        try {
            projectItemService.addProjectItem(projectItemDTO);
            return "등록 완료";
        } catch (Exception e) {
            logger.error("Error adding project item", e);
            return "등록 실패";
        }
    }

    // 프로젝트 아이템 상세 조회
    @GetMapping("/project-item/{project-item-id}")
    public Optional<ProjectItemDTO> getProjectItemInfo(@PathVariable(name = "project-item-id") Long projectItemId) throws Exception {
        try {
            return projectItemService.findByProjectItemId(projectItemId);
        } catch (Exception e) {
            logger.error("Error retrieving project item with id: {}", projectItemId, e);
            throw new Exception("프로젝트 아이템 상세정보를 가져오지 못했습니다.");
        }
    }

    // 프로젝트 아이템 수정
    @PutMapping("/project-item/{project-item-id}")
    public String updateProjectItem(@PathVariable(name = "project-item-id") Long projectItemId, @RequestBody ProjectItemDTO projectItemDTO) {
        try {
            projectItemService.updateProjectItem(projectItemId, projectItemDTO);
            return "프로젝트 아이템 수정 완료";
        } catch (Exception e) {
            logger.error("Error updating project item with id: {}", projectItemId, e);
            return "프로젝트 아이템 수정 실패";
        }
    }

    // 프로젝트 아이템 삭제
    @DeleteMapping("/project-item/{project-item-id}")
    public String deleteProjectItem(@PathVariable(name = "project-item-id") Long projectItemId) {
        try {
            projectItemService.deleteProjectItem(projectItemId);
            return "프로젝트 아이템 삭제 성공";
        } catch (Exception e) {
            logger.error("Error deleting project item with id: {}", projectItemId, e);
            return "프로젝트 아이템 삭제 실패";
        }
    }

//    @PostMapping("/{project-item-id}/request")
//    public String requestProjectMatching() {
//        logger.info("Request project matching");
//        return "";
//    }
}