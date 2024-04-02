package com.fisa.wooriarte.spacerental.controller;

import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.service.SpaceRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/space")
public class SpaceRentalController {
    @Autowired
    SpaceRentalService service;

    @PostMapping("")
    public String addSpaceRental(@RequestBody SpaceRentalDTO spaceRentalDTO){
        service.addSpaceRental(spaceRentalDTO);
        return "success";
    }

    @GetMapping("/{id}")
    public String getSpaceRentalInfo(@PathVariable("id") String id) {
        return service.findById(id).toString();
    }

    @PatchMapping("{id}")
    public String updateSpaceRentalInfo(@PathVariable("id") String id, @RequestBody SpaceRentalDTO spaceRentalDTO) {
        service.updateSpaceRental(id, spaceRentalDTO);
        return "success";
    }

    @GetMapping("{id}/{matching_status}")
    public String getSpaceRentalMatching(@PathVariable("id") String id, @PathVariable("matching_status") int status) {
        // 나중에 matching 생기면 추가할 내용
        return null;
    }
}
