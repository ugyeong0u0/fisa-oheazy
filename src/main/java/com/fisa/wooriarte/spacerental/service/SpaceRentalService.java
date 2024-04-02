package com.fisa.wooriarte.spacerental.service;

import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SpaceRentalService {
    @Autowired
    SpaceRentalRepository repository;

    @Transactional
    public boolean addSpaceRental(SpaceRentalDTO sr) {
        try {
            Optional<SpaceRental> optionalSpaceRental = repository.findBySpaceRentalId(sr.getId());
            optionalSpaceRental.ifPresent(a -> {
                throw new DataIntegrityViolationException("Duplicate User id");
            });
            repository.save(sr.toEntity());
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SpaceRentalDTO findById(String id) {
       SpaceRental spaceRental = repository.findBySpaceRentalId(id)
               .orElseThrow(() -> new IllegalArgumentException("Fail to search info. No one uses that ID"));
       return spaceRental.toDTO();
    }

    @Transactional
    public boolean updateSpaceRental(String id, SpaceRentalDTO spaceRentalDTO) throws Exception{
        SpaceRental spaceRental = repository.findBySpaceRentalId(id)
                .orElseThrow(() -> new IllegalArgumentException("Fail to update. No one uses that ID"));
        BeanUtils.copyProperties(spaceRentalDTO, spaceRental, "createAt", "businessId");
        try {
            repository.save(spaceRental);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteSpaceRental(String id) throws Exception {
        SpaceRental spaceRental = repository.findBySpaceRentalId(id)
                .orElseThrow(() -> new IllegalArgumentException(" Fail to delete. No one uses that ID."));
        if(spaceRental.isDeleted()) {
            throw new RuntimeException("Already deleted User");
        }
        spaceRental.setDeleted(true);
        try {
            repository.save(spaceRental);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
