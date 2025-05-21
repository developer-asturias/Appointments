package org.asturias.Infrastructure.Repositories;


import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaAppointmentRepository  extends JpaRepository<AppointmentsEntity, Long> {

    List<AppointmentsEntity> findByDateAppointmentBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);



}
