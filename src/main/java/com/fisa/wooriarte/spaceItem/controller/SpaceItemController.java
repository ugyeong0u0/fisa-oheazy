package com.fisa.wooriarte.spaceItem.controller;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/space-item")
public class SpaceItemController {

    private static final Logger logger = LoggerFactory.getLogger(SpaceItemController.class);

    private final SpaceItemService spaceItemService;

    @Autowired
    public SpaceItemController(SpaceItemService spaceItemService) {
        this.spaceItemService = spaceItemService;
    }

    // 모든 공간 아이템 정보 조회
    @GetMapping("")
    public List<SpaceItemDTO> getAllSpaceItemInfo() throws Exception {
        try {
            return spaceItemService.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all space items", e);
            throw new Exception("공간 아이템 정보를 가져오지 못했습니다.");
        }
    }

    // 공간 아이템 추가
    @PostMapping("")
    public String addSpaceItem(@RequestBody SpaceItemDTO spaceItemDTO) {
        try {
            spaceItemService.addSpaceItem(spaceItemDTO);
            return "등록 완료";
        } catch (Exception e) {
            logger.error("Error adding space item", e);
            return "등록 실패";
        }
    }

    // 공간 아이템 상세 조회
    @GetMapping("/{id}")
    public Optional<SpaceItemDTO> getSpaceItemInfo(@PathVariable Long id) throws Exception {
        try {
            return spaceItemService.findSpaceItembyId(id);
        } catch (Exception e) {
            logger.error("Error retrieving space item with id: {}", id, e);
            throw new Exception("공간 아이템 상세정보를 가져오지 못했습니다.");
        }
    }

    // 공간 아이템 수정
    @PatchMapping("/{id}")
    public String updateSpaceItem(@PathVariable Long id, @RequestBody SpaceItemDTO spaceItemDTO) {
        try {
            spaceItemService.updateSpaceItem(id, spaceItemDTO);
            return "공간 아이템 수정 완료";
        } catch (Exception e) {
            logger.error("Error updating space item with id: {}", id, e);
            return "공간 아이템 수정 실패";
        }
    }

    // 공간 아이템 삭제
    @DeleteMapping("/{id}")
    public String deleteSpaceItem(@PathVariable Long id) {
        try {
            spaceItemService.deleteSpaceItem(id);
            return "공간 아이템 삭제 성공";
        } catch (Exception e) {
            logger.error("Error deleting space item with id: {}", id, e);
            return "공간 아이템 삭제 실패";
        }
    }

    @PostMapping("/{id}/request")
    public String requestSpaceMatching() {
        // 이 부분은 구현의 예시가 되지 않았으므로, 구체적인 로직에 따라 예외 처리 및 로깅을 추가할 필요가 있습니다.
        logger.info("Request space matching");
        return "";
    }
}