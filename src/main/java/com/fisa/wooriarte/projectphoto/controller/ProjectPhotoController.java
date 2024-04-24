package com.fisa.wooriarte.projectphoto.controller;

import com.fisa.wooriarte.projectphoto.dto.ProjectPhotoDTO;
import com.fisa.wooriarte.projectphoto.service.ProjectPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/project-photos")
@RestController
public class ProjectPhotoController {

    private final ProjectPhotoService projectPhotoService;

    @Autowired
    public ProjectPhotoController(ProjectPhotoService projectPhotoService) {
        this.projectPhotoService = projectPhotoService;
    }

    /**
     * 1. 사진 파일 추가 - S3, DB
     * @param multipartFileList : 사진 file 리스트
     * @param  projectItemId : 매핑될 projectItemId
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/{project-item-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPhotos(@PathVariable("project-item-id") Long projectItemId, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) throws IOException {
        if (multipartFileList == null) {
            return ResponseEntity.badRequest().body("No files provided");
        } else {
            return projectPhotoService.addPhotos(multipartFileList, projectItemId);
        }
    }

    /**
     * 2. 사진 개별 삭제 - S3, DB
     * @param photoIds : 삭제할 photoIds
     * @return
     */
    @DeleteMapping("/delete-project-photo")
    public ResponseEntity<String> deletePhotos(@RequestParam List<Long> photoIds) {
        try {
            projectPhotoService.deletePhotosByProjectId(photoIds);
            return ResponseEntity.ok("Photos deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 3. spaceItemId에 해당하는 모든 사진 삭제 - S3, DB
     * @param projectItemId : 삭제할 사진을 가지고 있는 projectItemId
     * @return
     */
    @DeleteMapping("/{project-item-id}")
    public ResponseEntity<String> deleteAllPhotos(@PathVariable("project-item-id") Long projectItemId) {
        try {
            projectPhotoService.deleteAllPhotos(projectItemId);
            return ResponseEntity.ok("All photos with space item ID " + projectItemId + " deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     *  4. SpaceItemId에 해당하는 모든 사진 출력
     * @param projectItemId : 출력할 사진을 가지고 있는 projectItemId
     * @return photos 객체 정보 출력
     */
    @GetMapping("/{project-item-id}")
    public ResponseEntity<List<ProjectPhotoDTO>> getPhotosBySpaceItemId(@PathVariable("project-item-id") Long projectItemId) {
        List<ProjectPhotoDTO> photos = projectPhotoService.getPhotosByProjectItemId(projectItemId);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    /**
     * 5. 프로젝트 사진 수정 - S3, DB : 해당 프로젝트의 사진 데이터 삭제 후 덮어씌우기
     * @param projectItemId : 수정할 projectItemId
     * @param multipartFileList : 수정에서 삽입될 사진 파일 리스트
     * @return
     * @throws IOException
     */
    @PutMapping(path = "/{project-item-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editPhotos(@PathVariable("project-item-id") Long projectItemId, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) throws IOException {
        if (multipartFileList == null) {
            return ResponseEntity.badRequest().body("No files provided");
        } else {
            // 해당 프로젝트 아이템의 모든 사진 삭제
            projectPhotoService.deleteAllPhotos(projectItemId);

            // 새로운 사진 파일 추가
            return projectPhotoService.addPhotos(multipartFileList, projectItemId);
        }
    }
}