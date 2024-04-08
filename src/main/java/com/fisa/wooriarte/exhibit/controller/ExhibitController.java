package com.fisa.wooriarte.exhibit.controller;

import com.fisa.wooriarte.exhibit.dto.ExhibitDTO;
import com.fisa.wooriarte.exhibit.service.ExhibitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
public class ExhibitController {

    private final ExhibitService exhibitService;

    @Autowired
    public ExhibitController(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }

    @GetMapping({"/admin/exhibits", "/exhibits"})
    //모든 전시 출력
    public List<ExhibitDTO> findAllExhibit() {
        return exhibitService.findAllExhibit();
    }

    @GetMapping({"/admin/exhibits/{id}", "/exhibits/{id}"})
    public ExhibitDTO findExhibitById(@PathVariable Long id) {
        return exhibitService.findExhibitbyId(id).orElse(null);
    }

    @PostMapping("/addexhibit")
    public String addExhibit(@RequestBody ExhibitDTO exhibitDTO) {
        log.info("addexhibit :: {}",exhibitDTO);
        try {
            exhibitService.addExhibit(exhibitDTO);
            System.out.println("티켓 생성 완료");
            return "success";
        } catch (Exception e) {
            System.err.println("티켓 생성 중 오류 발생: " + e.getMessage());
            return "Failed to create ticket: ";
        }
    }

    @PatchMapping("/admin/exhibits/{id}")
    public String updateExhibit(@PathVariable String id, @RequestBody ExhibitDTO exhibitDTO) {
        try {
            log.info("editExhibit :: {}", exhibitDTO);
            exhibitService.updateExhibit(id, exhibitDTO);
            return "전시 수정 완료";
        } catch (Exception e) {
            log.error("Error updating space item with id: {}", id, e);
            return "전시 수정 실패";
        }
    }


    @DeleteMapping("/exhibit/{id}")
    public ResponseEntity<String> deleteExhibitById(@PathVariable Long id) {
        try {
            exhibitService.deleteExhibitById(id);
            return ResponseEntity.ok("Exhibit with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete exhibit with ID " + id + ". Error: " + e.getMessage());
        }
    }
}
