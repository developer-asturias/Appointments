package org.asturias.Domain.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Users {

    private Long id;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String numberDocument;


}
