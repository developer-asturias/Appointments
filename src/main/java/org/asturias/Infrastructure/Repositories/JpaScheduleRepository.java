package org.asturias.Infrastructure.Repositories;


import org.asturias.Infrastructure.Entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
}
