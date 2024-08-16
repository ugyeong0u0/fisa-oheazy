package com.fisa.wooriarte.jwt;

import com.fisa.wooriarte.admin.domain.Admin;
import com.fisa.wooriarte.admin.repository.AdminRepository;
import com.fisa.wooriarte.projectmanager.domain.ProjectManager;
import com.fisa.wooriarte.projectmanager.repository.ProjectManagerRepository;
import com.fisa.wooriarte.spacerental.domain.SpaceRental;
import com.fisa.wooriarte.spacerental.repository.SpaceRentalRepository;
import com.fisa.wooriarte.user.domain.User;
import com.fisa.wooriarte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final SpaceRentalRepository spaceRentalRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public CustomUserDetailsService( UserRepository userRepository,
            ProjectManagerRepository projectManagerRepository,
            SpaceRentalRepository spaceRentalRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.spaceRentalRepository = spaceRentalRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public CustomUserDetailsImpl loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<CustomUserDetailsImpl> userDetails = userRepository.findById(id)
                .map(this::createUserDetails);

        if (!userDetails.isPresent()) {
            userDetails = projectManagerRepository.findById(id)
                    .map(this::createUserDetails);
        }

        if (!userDetails.isPresent()) {
            userDetails = spaceRentalRepository.findById(id)
                    .map(this::createUserDetails);
        }

        if (!userDetails.isPresent()) {
            userDetails = adminRepository.findById(id)
                    .map(this::createUserDetails);
        }

        return userDetails.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private CustomUserDetailsImpl createUserDetails(Object user) {
        Long userId;
        String username;
        String password;
        Collection<? extends GrantedAuthority> authorities;
        boolean accountNonExpired = true; // 또는 실제 조건에 맞게 설정
        boolean accountNonLocked = true;  // 또는 실제 조건에 맞게 설정
        boolean credentialsNonExpired = true;  // 또는 실제 조건에 맞게 설정
        boolean enabled = true;  // 또는 실제 조건에 맞게 설정

        if (user instanceof ProjectManager) {
            ProjectManager manager = (ProjectManager) user;
            userId = manager.getProjectManagerId();  // ID 매핑이 필요하다고 가정합니다
            username = manager.getUsername();
            password = manager.getPassword();
            authorities = manager.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (user instanceof SpaceRental) {
            SpaceRental rental = (SpaceRental) user;
            userId = rental.getSpaceRentalId();  // ID 매핑이 필요하다고 가정합니다
            username = rental.getUsername();
            password = rental.getPassword();
            authorities = rental.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (user instanceof User) {
            User userEntity = (User) user;
            userId = userEntity.getUserId();  // ID 매핑이 필요하다고 가정합니다
            username = userEntity.getUsername();
            password = userEntity.getPassword();
            authorities = userEntity.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            userId = admin.getAdminId();  // ID 매핑이 필요하다고 가정합니다
            username = admin.getUsername();
            password = admin.getPassword();
            authorities = admin.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("지원되지 않는 사용자 유형입니다.");
        }

        return new CustomUserDetailsImpl(userId, username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

}

