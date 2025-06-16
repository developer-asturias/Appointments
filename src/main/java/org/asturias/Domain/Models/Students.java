package org.asturias.Domain.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.ProgramEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
public class Students {

    private Long id;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private String name;

    private String numberDocument;

    private String lastName;

    private String email;

    private String phone;

    private Program program;



}
