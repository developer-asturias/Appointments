package org.asturias.Infrastructure.Repositories;

import org.asturias.Infrastructure.Entities.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentsRepository extends JpaRepository<StudentsEntity, Long > {
}
