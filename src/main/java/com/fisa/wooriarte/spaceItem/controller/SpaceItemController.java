package com.fisa.wooriarte.spaceItem.controller;

import com.fisa.wooriarte.spaceItem.dto.SpaceItemDto;
import com.fisa.wooriarte.spaceItem.service.SpaceItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RequestMapping("/api/space-items")
@RestController
public class SpaceItemController {

    private static final Logger log = LoggerFactory.getLogger(SpaceItemController.class);

    private final SpaceItemService spaceItemService;

    @Autowired
    public SpaceItemController(SpaceItemService spaceItemService) {
        this.spaceItemService = spaceItemService;
    }

    // Retrieve all space items information
    @GetMapping("")
    public ResponseEntity<?> getAllSpaceItemInfo() {
        try {
            List<SpaceItemDto> items = spaceItemService.findAll();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error retrieving all space items", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to retrieve space item information."));
        }
    }

    @GetMapping("/space-rental/{space-rental-id}")
    public ResponseEntity<List<SpaceItemDto>> getSpaceItemBySpaceRentalId(@PathVariable("space-rental-id") Long spaceRentalId) {
        List<SpaceItemDto> spaceItemDtoList = spaceItemService.findBySpaceRentalId(spaceRentalId);
        return ResponseEntity.ok(spaceItemDtoList);
    }

    // Add a space item
    @PostMapping("")
    public ResponseEntity<?> addSpaceItem(@RequestBody SpaceItemDto spaceItemDTO) {
        try {
            spaceItemService.addSpaceItem(spaceItemDTO);
            return ResponseEntity.ok(Map.of("message", "Space item successfully added."));
        } catch (Exception e) {
            log.error("Error adding space item", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add space item."));
        }
    }

    // Retrieve detail of a space item
    @GetMapping("/{space-item-id}")
    public ResponseEntity<?> getSpaceItemInfo(@PathVariable(name = "space-item-id") Long spaceItemId) {
        try {
            SpaceItemDto spaceItemDto = spaceItemService.findSpaceItemById(spaceItemId);
            return ResponseEntity.ok(spaceItemDto);
        } catch (NoSuchElementException e) {
            log.error("Failed to retrieve space item with id: {}", spaceItemId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error retrieving space item with id: {}", spaceItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to retrieve space item details."));
        }
    }

    // Update a space item
    @PutMapping("/{space-item-id}")
    public ResponseEntity<?> updateSpaceItem(@PathVariable(name = "space-item-id") Long spaceItemId, @RequestBody SpaceItemDto spaceItemDTO) {
        try {
            spaceItemService.updateSpaceItem(spaceItemId, spaceItemDTO);
            return ResponseEntity.ok(Map.of("message", "Space item successfully updated."));
        } catch (Exception e) {
            log.error("Error updating space item with id: {}", spaceItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to update space item."));
        }
    }

    // Delete a space item
    @DeleteMapping("/{space-item-id}")
    public ResponseEntity<?> deleteSpaceItem(@PathVariable(name = "space-item-id") Long spaceItemId) {
        try {
            spaceItemService.deleteSpaceItem(spaceItemId);
            return ResponseEntity.ok(Map.of("message", "Space item successfully deleted."));
        } catch (Exception e) {
            log.error("Error deleting space item with id: {}", spaceItemId, e);
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to delete space item."));
        }
    }
}
