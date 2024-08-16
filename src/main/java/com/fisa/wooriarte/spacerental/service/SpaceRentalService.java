package com.fisa.wooriarte.spacerental.service;

import com.fisa.wooriarte.jwt.JwtToken;
import com.fisa.wooriarte.jwt.JwtTokenProvider;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.spacerental.dto.SpaceRentalDto;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SpaceRentalService {
    private final SpaceRentalRepository spaceRentalRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpaceRentalService(SpaceRentalRepository spaceRentalRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.spaceRentalRepository = spaceRentalRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 임대사업자 추가
     * @param spaceRentalDTO 추가할 임대사업자 정보
     * @return 임대사업자 추가 성공 여부
     */
    @Transactional
    public boolean addSpaceRental(SpaceRentalDto spaceRentalDTO) {
        Optional<SpaceRental> optionalSpaceRental = spaceRentalRepository.findById(spaceRentalDTO.getId());
        if (optionalSpaceRental.isPresent()) {
            throw new DataIntegrityViolationException("이미 존재하는 사용자 ID입니다.");
        }
        SpaceRental spaceRental = spaceRentalDTO.toEntity();
        if (spaceRentalDTO.getPwd() != null) {
            spaceRental.setPwd(passwordEncoder.encode(spaceRentalDTO.getPwd()));
        }
        spaceRental.addRole("SPACE_RENTAL");
        spaceRentalRepository.save(spaceRental);
        return true;
    }

    /**
     * JWT로 임대사업자 로그인
     * @param id  임대사업자 ID
     * @param pwd 임대사업자 비밀번호
     * @return JWT 토큰
     */
    @Transactional
    public JwtToken login(String id, String pwd) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * 이메일로 임대사업자 ID 검색
     * @param email 임대사업자 이메일
     * @return 임대사업자 ID
     */
    public String getId(String email) {
        SpaceRental spaceRental = spaceRentalRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("가입되지 않은 사용자입니다."));
        return spaceRental.getId();
    }

    /**
     * 임대사업자 비밀번호 재설정
     * @param id      임대사업자 ID
     * @param newPwd  새로운 비밀번호
     * @return 비밀번호 재설정 성공 여부
     */
    public boolean setPwd(String id, String newPwd) {
        SpaceRental spaceRental = spaceRentalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("가입되지 않은 사용자입니다."));
        spaceRental.setPwd(passwordEncoder.encode(newPwd));
        spaceRentalRepository.save(spaceRental);
        return true;
    }

    /**
     * ID로 임대사업자 정보 검색
     * @param spaceRentalId 임대사업자 ID
     * @return 임대사업자 정보 DTO
     */
    public SpaceRentalDto findBySpaceRentalId(Long spaceRentalId) {
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID를 사용하는 임대사업자를 찾을 수 없습니다."));
        return SpaceRentalDto.fromEntity(spaceRental);
    }

    /**
     * 임대사업자 정보 수정
     * @param spaceRentalId 임대사업자 ID
     * @param spaceRentalDTO 수정할 임대사업자 정보
     * @return 수정 성공 여부
     */
    @Transactional
    public boolean updateSpaceRental(Long spaceRentalId, SpaceRentalDto spaceRentalDTO) {
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID를 사용하는 임대사업자를 찾을 수 없습니다."));
        spaceRental.updateSpaceRental(spaceRentalDTO);
        spaceRentalRepository.save(spaceRental);
        return true;
    }

    /**
     * 임대사업자 삭제
     * @param spaceRentalId 삭제할 임대사업자 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deleteSpaceRental(Long spaceRentalId) {
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID를 사용하는 임대사업자를 찾을 수 없습니다."));
        if (spaceRental.getIsDeleted()) {
            throw new DataIntegrityViolationException("이미 삭제된 사용자입니다.");
        }
        spaceRental.setIsDeleted();
        spaceRentalRepository.save(spaceRental);
        return true;
    }

    /**
     * 임대사업자의 비밀번호를 검증
     * @param spaceRentalId 임대사업자 ID
     * @param pwd 확인할 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean verifyPassword(Long spaceRentalId, String pwd) {
        SpaceRental spaceRental = spaceRentalRepository.findById(spaceRentalId)
                .orElseThrow(() -> new NoSuchElementException("가입되지 않은 사용자입니다."));
        return passwordEncoder.matches(pwd, spaceRental.getPwd());
    }


    /**
     * 임대사업자 로그인 - jwt 로그인을 대체. 사용 x
     * @param id  임대사업자 ID
     * @param pwd 임대사업자 비밀번호
     * @return 로그인된 임대사업자 정보
     * @throws Exception 로그인 실패 시 예외 발생
     */
    public SpaceRentalDto loginSpaceRental(String id, String pwd) throws Exception {
        SpaceRental spaceRental = spaceRentalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("로그인 실패 - 사용자를 찾을 수 없습니다."));
        if (passwordEncoder.matches(pwd, spaceRental.getPwd())) {
            return SpaceRentalDto.fromEntity(spaceRental);
        } else {
            throw new Exception("로그인 불가 - 비밀번호가 일치하지 않습니다.");
        }
    }
}