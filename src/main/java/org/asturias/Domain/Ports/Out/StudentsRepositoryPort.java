package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Students;



import java.util.Optional;

public interface StudentsRepositoryPort {

    Students save (Students students);
    Optional <Students>  update (Long id, Students students);
    Optional<Students> findById (Long id);

}
