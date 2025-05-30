package org.asturias.Infrastructure.Adapters;


import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Ports.Out.ProgramRepositoryPort;
import org.asturias.Infrastructure.Entities.ProgramEntity;
import org.asturias.Infrastructure.Mappers.Entities.ProgramMapper;
import org.asturias.Infrastructure.Repositories.JpaProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaProgramRepositoryAdapter implements ProgramRepositoryPort {

    @Autowired
    private JpaProgramRepository jpaProgramRepository;

    @Autowired
    private ProgramMapper programMapper;



    @Override
    public Optional<Program> findById(Long id) {
        return  jpaProgramRepository.findById(id).map(typeOfAppointment -> programMapper.PROGRAM(typeOfAppointment));
    }

    @Override
    public List<Program> findAll() {
        List<ProgramEntity> typeOfAppointment = jpaProgramRepository.findAll();
        return programMapper.PROGRAM_LIST(typeOfAppointment);
    }
}
