package com.fisa.wooriarte.user.dto;


import com.fisa.wooriarte.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDTO {
    private Long userId;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private Boolean deleted;


    public User toEntity(){
        return User.builder()
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .build();
    }
    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .deleted(user.getDeleted())
                .build();
    }
}
