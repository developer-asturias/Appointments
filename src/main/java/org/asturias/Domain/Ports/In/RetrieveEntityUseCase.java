package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RetrieveEntityUseCase {

//    Page<Users> getAllUsers(String name, Pageable pageable);
    Optional<Users> getUserById(Long id);
    Optional<Appointments> findAppointmentById (Long id);
    List<Appointments> findByDateAppointmentBetween(String start, String end);
    Optional<DetailsAppointmentDTO> findDetailsAppointmentById(Long id);
    List<Program> FindAllProgram ();
    List<TypeOfAppointment> FindAllTypeAppointment();
    Optional<Students> getStudentById(Long id);
    List<Schedule> FindAllSchedule();
    Optional<Schedule> getScheduleById(Long id);



}
