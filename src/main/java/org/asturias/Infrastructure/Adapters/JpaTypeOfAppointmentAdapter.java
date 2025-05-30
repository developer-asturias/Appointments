package org.asturias.Infrastructure.Adapters;

import org.asturias.Domain.Models.TypeOfAppointment;
import org.asturias.Domain.Ports.Out.TypeOfAppointmentRepositoryPort;
import org.asturias.Infrastructure.Entities.TypeOfAppointmentEntity;
import org.asturias.Infrastructure.Mappers.Entities.TypeAppointmentMapper;
import org.asturias.Infrastructure.Repositories.JpaTypeOfAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class JpaTypeOfAppointmentAdapter implements TypeOfAppointmentRepositoryPort {

    @Autowired
    private JpaTypeOfAppointmentRepository jpaTypeOfAppointmentRepository;

    @Autowired
    private TypeAppointmentMapper typeAppointmentMapper;



    @Override
    public Optional<TypeOfAppointment> findById(Long id) {
        return  jpaTypeOfAppointmentRepository.findById(id).map(typeOfAppointment -> typeAppointmentMapper.TYPE_OF_APPOINTMENT(typeOfAppointment));
    }

    @Override
    public List<TypeOfAppointment> findAll() {
        List<TypeOfAppointmentEntity> typeOfAppointment = jpaTypeOfAppointmentRepository.findAll();
        return typeAppointmentMapper.TYPE_OF_APPOINTMENTS(typeOfAppointment);
    }
}
