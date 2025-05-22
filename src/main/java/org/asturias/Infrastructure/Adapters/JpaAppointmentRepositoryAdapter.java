package org.asturias.Infrastructure.Adapters;


import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.asturias.Infrastructure.Mappers.Entities.AppointmentMapper;
import org.asturias.Infrastructure.Repositories.JpaAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class JpaAppointmentRepositoryAdapter  implements AppointmentsRepositoryPort {


    @Autowired
    private JpaAppointmentRepository jpaAppointmentRepository;

    @Autowired
    private AppointmentMapper   appointmentMapper;


    public JpaAppointmentRepositoryAdapter(JpaAppointmentRepository jpaAppointmentRepository, AppointmentMapper appointmentMapper) {
        this.jpaAppointmentRepository = jpaAppointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public Optional<Appointments> update(Appointments appointments) {
        if (jpaAppointmentRepository.existsById(appointments.getId())){
            AppointmentsEntity appointmentsEntity = appointmentMapper.APPOINTMENTS_ENTITY(appointments);
            AppointmentsEntity updatedAppointmemt= jpaAppointmentRepository.save(appointmentsEntity);
            return Optional.of(appointmentMapper.APPOINTMENTS(updatedAppointmemt));
        }
        return  Optional.empty();
    }

    @Override
    public Optional<Appointments> findById(Long id) {
        return jpaAppointmentRepository.findById(id).map(appointmentMapper::APPOINTMENTS);
    }

    @Override
    public Appointments save(Appointments appointments) {
        AppointmentsEntity appointmentsEntity = appointmentMapper.APPOINTMENTS_ENTITY(appointments);
        return appointmentMapper.APPOINTMENTS(jpaAppointmentRepository.save(appointmentsEntity));
    }

    @Override
    public List<Appointments> findByDateAppointmentBetween(String start, String end) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);


        return jpaAppointmentRepository.findByDateAppointmentBetween(startDate, endDate)
                .stream().map(appointmentMapper::APPOINTMENTS)
                .toList();
    }

}
