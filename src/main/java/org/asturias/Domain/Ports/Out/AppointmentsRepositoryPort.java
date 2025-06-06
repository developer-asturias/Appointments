package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.DTO.Response.AppointmentsPageableResponseDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentsRepositoryPort {

    Optional <Appointments> update (Appointments appointments);
    Optional<Appointments> findById (Long id);
    Appointments save (Appointments appointments);
    List<Appointments> findByDateAppointmentBetween(String start, String end);
    Optional<DetailsAppointmentDTO>  findDetailsAppointmentById (Long id);
    List<Appointments> findAppointmentsByStudentsId(Long studentId);
    Page<AppointmentsPageableResponseDTO> findAllPageable(StatusAppointment status, Pageable pageable);
    Optional<Appointments> updateAppointmentMentor(Long id, Long mentorId);



}
