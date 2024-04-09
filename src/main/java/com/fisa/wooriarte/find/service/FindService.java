//package com.fisa.wooriarte.find.service;
//
//import com.fisa.wooriarte.find.dto.request.FindBusinessIdRequest;
//import com.fisa.wooriarte.find.dto.request.FindBusinessPassRequest;
//import com.fisa.wooriarte.find.repository.FindRepository;
//import com.fisa.wooriarte.spacerental.domain.SpaceRental;
//import com.fisa.wooriarte.spacerental.dto.SpaceRentalDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FindService {
//    @Autowired
//    private FindRepository findRepository;
//
//    public SpaceRentalDTO getBusinessId(FindBusinessIdRequest businessDTO) {
//        try {
//            final SpaceRental response = findRepository.findBySpaceRentalId(businessDTO.getEmail(), businessDTO.getBusinessNumber())
//                    .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));
//            return response.toDTO();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public SpaceRentalDTO getBusinessPw(FindBusinessPassRequest businessDTO) {
//        try {
//            final SpaceRental response = findRepository.findBySpaceRentalId(businessDTO.getEmail(), businessDTO.getBusinessNumber(), businessDTO.getId())
//                    .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));
//            return response.toDTO();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}
