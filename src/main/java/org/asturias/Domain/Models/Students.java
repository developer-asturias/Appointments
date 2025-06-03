package org.asturias.Domain.Models;

import lombok.Getter;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.ProgramEntity;


@Setter
@Getter
public class Students {


    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private String numberDocument;

    private ProgramEntity program;

    private Long programId;

    public Students(Long id, String name, String lastName, String email, String phone, String numberDocument, ProgramEntity program, Long programId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.numberDocument = numberDocument;
        this.program = program;
        this.programId = programId;
    }

}
