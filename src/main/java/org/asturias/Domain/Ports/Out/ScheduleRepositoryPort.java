package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryPort {
    Schedule save (Schedule schedule);
    Optional <Schedule>  update (Long id, Schedule schedule);
    Optional<Schedule> findById (Long id);
    List<Schedule> findAll();
}
