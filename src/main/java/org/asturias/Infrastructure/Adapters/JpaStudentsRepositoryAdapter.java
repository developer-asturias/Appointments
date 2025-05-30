package org.asturias.Infrastructure.Adapters;

import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Ports.Out.StudentsRepositoryPort;
import org.asturias.Infrastructure.Entities.StudentsEntity;
import org.asturias.Infrastructure.Mappers.Entities.StudentsMapper;
import org.asturias.Infrastructure.Repositories.JpaStudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaStudentsRepositoryAdapter  implements StudentsRepositoryPort {


    @Autowired
    private JpaStudentsRepository jpaStudentsRepository;

    @Autowired
    private StudentsMapper studentsMapper;


    public JpaStudentsRepositoryAdapter(JpaStudentsRepository jpaStudentsRepository, StudentsMapper studentsMapper) {
        this.jpaStudentsRepository = jpaStudentsRepository;
        this.studentsMapper = studentsMapper;
    }

    @Override
    public Students save(Students students) {
        StudentsEntity studentsEntity = studentsMapper.STUDENTS_ENTITY(students);
        return studentsMapper.STUDENTS(jpaStudentsRepository.save(studentsEntity));
    }

    @Override
    public Optional <Students> update(Long id, Students students) {
        if(jpaStudentsRepository.existsById(id)){
            StudentsEntity studentsEntity = studentsMapper.STUDENTS_ENTITY(students);
            StudentsEntity studentUpdate = jpaStudentsRepository.save(studentsEntity);
            return Optional.of(studentsMapper.STUDENTS(studentUpdate));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Students> findById(Long id) {
        return jpaStudentsRepository.findById(id).map(studentsEntity -> studentsMapper.STUDENTS(studentsEntity));
    }

}
