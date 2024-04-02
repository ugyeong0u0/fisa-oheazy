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
    private long userId;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createAt, updateAt;
    private boolean deleted;


    public User toEntity(){
        return User.builder().id(this.id).pwd(this.pwd).name(this.name).email(this.email).phone(this.phone).build();
    }
}
