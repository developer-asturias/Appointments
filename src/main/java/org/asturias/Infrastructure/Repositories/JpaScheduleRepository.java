package org.asturias.Infrastructure.Repositories;


import org.asturias.Infrastructure.Entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface JpaScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findByDayOfWeekAndTypeOfAppointmentId(int dayOfWeek, Long typeOfAppointmentId);
}
