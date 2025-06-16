package org.asturias.Domain.Models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.RoleEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private Long id;

    private String name;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private String password;

    private String email;

    private String documentNumber;

    private boolean enabled;

    private LocalDateTime lastLogin;



}
