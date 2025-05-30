package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.TypeOfAppointment;

import java.util.List;
import java.util.Optional;

public interface ProgramRepositoryPort {

    Optional<Program> findById (Long id);
    List<Program> findAll();
}
