package org.asturias.Domain.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.ScheduleEntity;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfAppointment {

    private Long id;

    private String name;

    private String description;

    private List<ScheduleEntity> schedules;
}
