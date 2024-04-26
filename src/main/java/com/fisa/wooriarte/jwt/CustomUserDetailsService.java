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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final SpaceRentalRepository spaceRentalRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public CustomUserDetailsService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            ProjectManagerRepository projectManagerRepository,
            SpaceRentalRepository spaceRentalRepository, AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.projectManagerRepository = projectManagerRepository;
        this.spaceRentalRepository = spaceRentalRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findById(id)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (userDetails != null) {
            return userDetails;
        }

        userDetails = projectManagerRepository.findById(id)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (userDetails != null) {
            return userDetails;
        }

        userDetails = spaceRentalRepository.findById(id)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (userDetails != null) {
            return userDetails;
        }

        return adminRepository.findById(id)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Object user) {
        String username;
        String password;
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user instanceof ProjectManager) {
            ProjectManager manager = (ProjectManager) user;
            username = manager.getUsername();
            password = manager.getPassword();
            authorities = manager.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            System.out.println(authorities);
        } else if (user instanceof SpaceRental) {
            SpaceRental rental = (SpaceRental) user;
            username = rental.getUsername();
            password = rental.getPassword();
            // 예시로, SpaceRental 엔티티의 권한 설정 방식을 가정합니다.
            authorities = rental.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (user instanceof User) {
            User userEntity = (User) user;
            username = userEntity.getUsername();
            password = userEntity.getPassword();
            // User 엔티티의 권한 설정 방식을 가정합니다.
            authorities = userEntity.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            username = admin.getUsername();
            password = admin.getPassword();
            authorities = admin.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(authorities)
                .build();
    }
}
