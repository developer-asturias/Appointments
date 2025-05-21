package org.asturias.Infrastructure.Repositories;

import org.asturias.Infrastructure.Entities.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProgramRepository extends JpaRepository<ProgramEntity, Long> {
}
