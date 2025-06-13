package org.asturias.Infrastructure.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "type_of_appointment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfAppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @Column(nullable = false, name = "duration_minutes")
    private Integer durationMinutes;


    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "typeOfAppointment", fetch = FetchType.LAZY)
    private List<ScheduleEntity> schedules;

    public TypeOfAppointmentEntity() {
    }

}
