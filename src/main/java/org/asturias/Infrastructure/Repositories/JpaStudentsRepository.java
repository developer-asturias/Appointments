package org.asturias.Infrastructure.Repositories;

import org.asturias.Infrastructure.Entities.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStudentsRepository extends JpaRepository<StudentsEntity, Long > {


    Optional<StudentsEntity>  findByEmailAndNumberDocument(String email, String numberDocument);

    boolean existsByEmailAndNumberDocument(String email, String numberDocument);

}
