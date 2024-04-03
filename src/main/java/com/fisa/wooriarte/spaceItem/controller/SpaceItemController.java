package com.fisa.wooriarte.spaceItem.controller;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDTO;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/space-item")
public class SpaceItemController {

    private final SpaceItemService spaceItemService;
    private final SpaceItemRepository spaceItemRepository;
    @Autowired
    public SpaceItemController(SpaceItemRepository spaceItemRepository, SpaceItemService spaceItemService) {
        this.spaceItemRepository = spaceItemRepository;
        this.spaceItemService = spaceItemService;
    }

    @GetMapping("")
    public List<SpaceItemDTO> getAllSpaceItemInfo() {
        return spaceItemService.findAll();
    }

    @PostMapping("")
    public String addSpaceItem(@RequestBody SpaceItemDTO spaceItemDTO) {
            spaceItemService.addSpaceItem(spaceItemDTO);
        return  "complete";
    }

    // 공간 아이템 상세 조회
    @GetMapping("/{id}")
    public Optional<SpaceItemDTO> getSpaceItemInfo(@PathVariable Long id) {
        return spaceItemService.findSpaceItembyId(id);
    }

    // 공간 아이템 수정
//    @PatchMapping("/{id}")
//    public String updateSpaceItem(@PathVariable Long id, @RequestBody SpaceItemDTO spaceItemDTO) {
//        return spaceItemService.updateSpaceItem(id, spaceItemDTO) ? "update complete" : "update failed";
//    }

//    @PostMapping("/{id}/request")
//        public String requestSpaceMatching() {
//
//        return  "";
//    }

}
