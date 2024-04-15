package com.fisa.wooriarte.user.service;

import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.dto.UserDTO;
import com.fisa.wooriarte.user.dto.request.UserInfoRequest;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Spring Security를 사용하여 비밀번호를 인코딩하고 비교하기 위해 PasswordEncoder 의존성 추가
    //;; Bean등록 해야함 ;;
    //일단보류
//    @Autowired
//    private PasswordEncoder passwordEncoder;


//    ----------------------------------------------------


    //회원가입
    // 로직 boolean으로 반환
    public boolean addUser(UserDTO userDTO) {
        //userDTO.toEntity 를 User userEntity로 변환한 이유는
        //클라이언트로부터 받은 데이터를 db에 저장하기 위함
        User userEntity = userDTO.toEntity();

        Optional<User> userEmail = userRepository.findUserByEmail(userEntity.getEmail());

        if (userEmail.isPresent()) {
            throw new IllegalStateException("회원가입 불가능(이메일 중복)");
        }

        Optional<User> userId = userRepository.findUserById(userEntity.getId());
        if (userId.isPresent()) {
            throw new IllegalStateException("회원가입 불가능 (아이디 중복)");
        }

        userRepository.save(userEntity);
        System.out.println("회원가입 가능");
        return true;
    }


    //비밀번호 검증
    public boolean verifyPassword(Long id, String password) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));

        if (password.equals(user.getPwd())) {
            System.out.println("비밀번호 일치");
            return true; //비밀번호 일치 -> true 반환
        } else {
            return false; //비밀번호 불일치 -> false 반환
        }

    }


    // 유저 개인 정보 단건 조회
    public UserDTO getMyUser(Long userId) {
        try {
            final User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("해당 id 유저가 없습니다."));
            return UserDTO.fromEntity(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 로그인
    public UserDTO loginUser(String id, String pwd) throws NoSuchElementException, IllegalAccessException {
        Optional<User> optionalUser = userRepository.findUserById(id);
        UserDTO userdto = null;
        if (optionalUser.isPresent() && optionalUser.get().getPwd().equals(pwd)) {
            userdto = UserDTO.fromEntity(optionalUser.get());
            return userdto;
        } else {
            throw new IllegalAccessException("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");

        }}


        // 유저 개인 정보 수정
        public Boolean updateMyUser (Long id, UserInfoRequest userInfoRequest){
            try {

                Optional<User> optionalUser = userRepository.findAllByUserId(id);

                if (!optionalUser.isPresent()) { // 유저 존재 확인
                    throw new IllegalStateException("없는 유저입니다."); // 사용자가 존재하지 않으면 예외 발생
                }

                // 이메일 중복 확인
                Optional<User> userEmail = userRepository.findUserByEmail(userInfoRequest.getEmail());
                if (userEmail.isPresent()) {
                    System.out.println("수정 불가능(이메일 중복)");
                    return false;
                }

                // 이메일이 중복되지 않은 경우, 엔터티 업데이트
                final int result = userRepository.updateAllById(id, userInfoRequest.getId(),
                        userInfoRequest.getPwd(), userInfoRequest.getName(), userInfoRequest.getEmail(), userInfoRequest.getPhone());
                System.out.println("변경된 엔터티 개수" + result);
                return true;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 유저 아이디 찾기
        public String findUserId (String name, String email){
            User userInfo = userRepository.findByNameAndEmail(name, email)
                    .orElseThrow(() -> new NoSuchElementException("유저 아이디를 찾을 수 없습니다.")); //객체 없으면 에러던지기

            return userInfo.getId(); //아이디 던저주기
        }

        // 유저 비밀번호 찾기
        public String findUserPw (String id, String name, String email){
            User userInfo = userRepository.findUserByIdAndNameAndEmail(id, name, email)
                    .orElseThrow(() -> new NoSuchElementException("유저 비밀번호를 찾을 수 없습니다.")); //객체 없으면 에러던지기

            return userInfo.getPwd(); //아이디 던저주기
        }


        //유저 삭제 ~
        //user_id 검색 후 delete 상태 변경
        @Transactional
        public void deleteUser (Long userId){
            //user_id로 검색
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("없는 유저입니다."));
            if (user.getIsDeleted()) {
                throw new DataIntegrityViolationException("Already deleted User");
            }
            user.setIsDeleted();
            userRepository.save(user);
        }
    }

