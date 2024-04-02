package com.fisa.wooriarte.spacerental.service;

import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceRentalService {
    @Autowired
    SpaceRentalRepository repository;

    public void addSpaceRental(SpaceRentalDTO sr) {
        repository.save(sr.toEntity());
    }

    public SpaceRentalDTO findById(String id) {
        return repository.findBySpaceRentalId(id).toDTO();
    }

    public void updateSpaceRental(String id, SpaceRentalDTO spaceRentalDTO) {
        SpaceRental spaceRental = repository.findBySpaceRentalId(id);


    }
}
