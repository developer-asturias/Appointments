package org.asturias.Infrastructure.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@Entity
@Table(name = "Users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @NotBlank(message = "el campo NOMBRE  no debe ser nulo")
    @Column(name = "name", length = 300)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "create_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updateAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "number_document")
    private String numberDocument;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();


    public UsersEntity() {
    }

    public UsersEntity(Long id, String password, String name, String email, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime lastLogin, String numberDocument, Set<RoleEntity> roles) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.lastLogin = lastLogin;
        this.numberDocument = numberDocument;
        this.roles = roles;
    }


}