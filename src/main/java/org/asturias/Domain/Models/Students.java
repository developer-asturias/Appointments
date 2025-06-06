package org.asturias.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.ProgramEntity;


@Setter
@Getter
@AllArgsConstructor
public class Students {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private String numberDocument;

    private Program program;

    private Long programId;


}
