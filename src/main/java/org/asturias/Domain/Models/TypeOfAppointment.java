package org.asturias.Domain.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TypeOfAppointment {

    private Long id;

    private String name;

    private String description;

    public TypeOfAppointment(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    public TypeOfAppointment() {
    }
}
