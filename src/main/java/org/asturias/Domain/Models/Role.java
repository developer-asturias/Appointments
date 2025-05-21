package org.asturias.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.RoleUser;


@Getter
@Setter
@AllArgsConstructor
public class Role {

    private Integer id;

    private RoleUser name;

}
