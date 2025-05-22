package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Appointments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentsRepositoryPort {

    Optional <Appointments> update (Appointments appointments);
    Optional<Appointments> findById (Long id);
    Appointments save (Appointments appointments);
    List<Appointments> findByDateAppointmentBetween(String start, String end);


}
