package org.asturias.Infrastructure.Repositories;

import org.asturias.Infrastructure.Entities.TypeOfAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTypeOfAppointmentRepository extends JpaRepository<TypeOfAppointmentEntity, Long> {
}
