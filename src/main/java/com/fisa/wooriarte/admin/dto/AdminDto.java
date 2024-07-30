package com.fisa.wooriarte.admin.dto;

import com.fisa.wooriarte.admin.domain.Admin;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDto {

    private Long adminId;
    private String id;
    private String pwd;
    private List<String> roles;
    private String name;

    // DTO를 엔티티로 변환
    public Admin toEntity() {
        return Admin.builder()
                .adminId(adminId)
                .id(id)
                .pwd(pwd)
                .roles(roles)
                .name(name)
                .build();
    }

    // 엔티티를 DTO로 변환
    public static AdminDto fromEntity(Admin admin) {
        return AdminDto.builder()
                .adminId(admin.getAdminId())
                .id(admin.getUsername()) // getUsername()은 id 필드를 반환
                .pwd(admin.getPassword()) // getPassword()는 pwd 필드를 반환
                .roles(admin.getRoles())
                .name(admin.getName())
                .build();
    }
}
