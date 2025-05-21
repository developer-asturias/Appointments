package org.asturias.Domain.Ports.In;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RetrieveEntityUseCase {

//    Page<Users> getAllUsers(String name, Pageable pageable);
    Optional<Users> getUserById(Long id);
    Optional<Appointments> findAppointmentById (Long id);
    List<Appointments> findByDateAppointmentBetween(LocalDateTime start, LocalDateTime end);
}
