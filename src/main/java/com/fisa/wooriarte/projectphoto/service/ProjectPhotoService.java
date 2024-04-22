package com.fisa.wooriarte.projectphoto.service;

import com.fisa.wooriarte.S3.utils.S3Service;
import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectItem.repository.ProjectItemRepository;
import com.fisa.wooriarte.projectphoto.domain.ProjectPhoto;
import com.fisa.wooriarte.projectphoto.dto.ProjectPhotoDTO;
import com.fisa.wooriarte.projectphoto.repository.ProjectPhotoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectPhotoService {
    private final S3Service s3Service;
    private final ProjectPhotoRepository projectPhotoRepository;

    private final ProjectItemRepository projectItemRepository;

    public ProjectPhotoService(S3Service s3Service, ProjectPhotoRepository projectPhotoRepository, ProjectItemRepository projectItemRepository) {
        this.s3Service = s3Service;
        this.projectPhotoRepository = projectPhotoRepository;
        this.projectItemRepository = projectItemRepository;
    }

    /**
     * 1. 사진 파일 저장 :ProjectItemId를 받아서 Photo파일 추가
     * @param multipartFileList : 사진 파일 리스트
     * @param id : ProjectItemId
     * @return
     * @throws IOException
     */
    public ResponseEntity<?> addPhotos(List<MultipartFile> multipartFileList, Long id) throws IOException {
        // 리스트로 들어온 여러 파일들을 하나씩 S3와 DB에 저장
        for (MultipartFile multipartFile : multipartFileList) {
            // 파일명 지정 (겹치지 않도록 UUID와 원본 파일명을 조합)
            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

            // S3에 파일 업로드
            String url = s3Service.upload(multipartFile, fileName);

            // S3 키 이름 추출
            String s3KeyName = extractKeyNameFromUrl(url);

            // ProjectPhotoDTO 생성
            ProjectPhotoDTO projectPhotoDTO = ProjectPhotoDTO.builder()
                    .projectItemId(id)
                    .fileName(fileName)
                    .url(url)
                    .s3KeyName(s3KeyName)
                    .build();

            // ProjectPhotoDTO를 엔티티로 변환하여 저장
            ProjectPhoto projectPhoto = projectPhotoDTO.toEntity(projectItemRepository);
            projectPhotoRepository.save(projectPhoto);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 2. 사진 개별 삭제 메서드 : ProjectPhotoId에 해당하는 사진 삭제
     * @param projectPhotoIds : 삭제할 사진의 projectPhotoIds
     */
    @Transactional
    public void deletePhotosByProjectId(List<Long> projectPhotoIds) {
        for (Long projectPhotoId : projectPhotoIds) {
            // DB에서 사진 정보 가져오기
            ProjectPhoto projectPhoto = projectPhotoRepository.findById(projectPhotoId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project photo ID: " + projectPhotoId));

            // S3 키 추출
            String keyName = extractKeyNameFromUrl(projectPhoto.getUrl());

            // S3에서 파일 삭제
            s3Service.delete(keyName);

            // DB에서 사진 삭제
            projectPhotoRepository.delete(projectPhoto);
        }
    }

    /**
     * 3. 입력한 ProjectItemId에 해당하는 모든 사진 삭제
     * @param projectItemId ProjectItem ID
     * @throws IllegalArgumentException 주어진 ProjectItem ID에 해당하는 사진이 없는 경우 예외 발생
     */
    public void deleteAllPhotos(Long projectItemId) {
        Optional<ProjectItem> projectItem = projectItemRepository.findById(projectItemId);
        List<ProjectPhoto> photos = projectPhotoRepository.findByProjectItem(projectItem);
        if (photos.isEmpty()) {
            throw new IllegalArgumentException("No photos found with project item ID: " + projectItemId);
        }
        for (ProjectPhoto photo : photos) {
            // 각 사진의 S3 키 추출
            String keyName = extractKeyNameFromUrl(photo.getUrl());

            // S3에서 파일 삭제
            s3Service.delete(keyName);
        }
        // DB에서 모든 사진 삭제
        projectPhotoRepository.deleteAll(photos);
    }

    /**
     * 4. DB에서 입력한 ProjectItemId에 해당하는 사진을 검색하는 메서드
     * @param projectItemId : 검색할 사진이 저장된 DB의 ProjectItemId
     * @return : 검색된 사진 리스트
     */
    public List<ProjectPhotoDTO> getPhotosByProjectItemId(Long projectItemId) {
        Optional<ProjectItem> projectItem = projectItemRepository.findById(projectItemId);
        List<ProjectPhoto> photos = projectPhotoRepository.findByProjectItem(projectItem);
        if (photos.isEmpty()) {
            throw new IllegalArgumentException("No photos found with project item ID: " + projectItemId);
        }
        return photos.stream()
                .map(ProjectPhotoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 5. S3 파일 URL에서 키 값 추출
     * @param fileUrl 파일 URL
     * @return S3 키 이름
     */
    private String extractKeyNameFromUrl(String fileUrl) {
        // 예: https://example-bucket.s3.amazonaws.com/folder/file.txt
        // 키 이름: folder/file.txt
        String[] parts = fileUrl.split("/");
        int length = parts.length;
        return String.join("/", Arrays.copyOfRange(parts, 3, length));
    }
}
