package com.fisa.wooriarte.projectmanager.domain;

import com.fisa.wooriarte.projectItem.domain.ProjectItem;
import com.fisa.wooriarte.projectmanager.DTO.ProjectManagerDTO;
import com.fisa.wooriarte.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectManagerId;

    @Column(nullable = false, unique = true)
    private Long businessNumber;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pwd;

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
    private Boolean deleted;

//    // projectItem 엔티티를 참조하는 필드 추가
//    @OneToMany(mappedBy = "ProjectItem", fetch = FetchType.EAGER)
//    private List<ProjectItem> projectItem;

    public void setDeleted(){this.deleted=!this.deleted;}

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
