package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/space-rental")
public class SpaceRentalController {
    @Autowired
    SpaceRentalService service;

    @PostMapping("")
    public String addSpaceRental(@RequestBody SpaceRentalDTO spaceRentalDTO) throws Exception {
        if(service.addSpaceRental(spaceRentalDTO))
            return "success";
        return "fail";
    }

    @GetMapping("/{id}")
    public String getSpaceRentalInfo(@PathVariable("id") String id) throws Exception{
        try {
            SpaceRentalDTO spaceRentalDTO = service.findById(id);
            return spaceRentalDTO.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @PatchMapping("{id}")
    public String updateSpaceRentalInfo(@PathVariable("id") String id, @RequestBody SpaceRentalDTO spaceRentalDTO) throws Exception {
        try {
            service.updateSpaceRental(id, spaceRentalDTO);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @GetMapping("{id}/{matching_status}")
    public String getSpaceRentalMatching(@PathVariable("id") String id, @PathVariable("matching_status") int status) {
        // 나중에 matching 생기면 추가할 내용
        return null;
    }

    @DeleteMapping("{id}")
    public String deleteSpaceRental(@PathVariable("id") String id) {
        try {
            service.deleteSpaceRental(id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


    @ExceptionHandler({DataIntegrityViolationException.class, IllegalArgumentException.class})
    public void handle(Exception e) {
        System.err.println(e.getMessage());
    }
}
