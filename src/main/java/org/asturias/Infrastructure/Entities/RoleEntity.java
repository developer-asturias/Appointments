package org.asturias.Infrastructure.Entities;

import jakarta.persistence.*;
import org.asturias.Domain.Enums.RoleUser;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleUser name;


    public RoleEntity() {
    }

    public RoleEntity(Integer id, RoleUser name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleUser getName() {
        return name;
    }

    public void setName(RoleUser name) {
        this.name = name;
    }
}
