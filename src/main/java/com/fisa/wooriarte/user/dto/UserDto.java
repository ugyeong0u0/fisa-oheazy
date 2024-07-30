package com.fisa.wooriarte.user.dto;


import com.fisa.wooriarte.user.domain.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private Long userId;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private Boolean isDeleted;


    public User toEntity(){
        return User.builder()
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .isDeleted(this.isDeleted)
                .build();
    }
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
