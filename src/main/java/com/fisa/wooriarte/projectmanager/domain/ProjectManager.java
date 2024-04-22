package com.fisa.wooriarte.projectmanager.domain;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.dto.ProjectManagerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProjectManager implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long projectManagerId;

    @OneToMany(mappedBy = "projectManager", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProjectItem> projectItems = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long businessNumber;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pwd;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public void setIsDeleted(){
        this.isDeleted=!this.isDeleted;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void updateProjectManager(ProjectManagerDto projectManagerDTO) {
        this.businessNumber = projectManagerDTO.getBusinessNumber();
        this.company = projectManagerDTO.getCompany();
        this.ceo = projectManagerDTO.getCeo();
        this.email = projectManagerDTO.getEmail();
        this.phone = projectManagerDTO.getPhone();
        this.pwd=projectManagerDTO.getPwd();
        this.id = projectManagerDTO.getId();
    }

    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    // UserDetails 상속으로 인한 메서드들
    @Override
    public String getPassword() {
        return this.pwd; // pwd 필드 반환
    }

    @Override
    public String getUsername() {
        return this.id; // id 필드 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부, 실제 비즈니스 로직에 따라 결정
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isDeleted; // isDeleted가 false일 때 계정이 잠기지 않음을 의미
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부, 실제 비즈니스 로직에 따라 결정
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted; // isDeleted가 false일 때 계정이 활성화됨을 의미
    }
}
