package org.asturias.Infrastructure.Repositories;


import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaAppointmentRepository  extends JpaRepository<AppointmentsEntity, Long> {

    List<AppointmentsEntity> findByDateAppointmentBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<AppointmentsEntity> findByStudentId(Long studentId);

    Page<AppointmentsEntity> findByStatusNot(StatusAppointment status, Pageable pageable);
}
