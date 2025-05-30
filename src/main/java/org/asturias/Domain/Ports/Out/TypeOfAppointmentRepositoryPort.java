package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.TypeOfAppointment;

import java.util.List;
import java.util.Optional;

public interface TypeOfAppointmentRepositoryPort {

    Optional<TypeOfAppointment> findById (Long id);

    List<TypeOfAppointment> findAll();
}
