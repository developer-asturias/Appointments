package org.asturias.Infrastructure.Adapters;


import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.asturias.Infrastructure.Mappers.Entities.AppointmentMapper;
import org.asturias.Infrastructure.Mappers.Response.DetailsAppointmentMapper;
import org.asturias.Infrastructure.Repositories.JpaAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private DetailsAppointmentMapper detailsAppointmentMapper;



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

    @Override
    public Optional<DetailsAppointmentDTO> findDetailsAppointmentById(Long id) {
        // 1. Obtener el modelo Appointment por su ID
//        Optional<Appointments> appointmentOpt = findById(id);
//
//        // 2. Si no existe la cita, retornar Optional vacío
//        if (appointmentOpt.isEmpty()) {
//            return Optional.empty();
//        }
//
//        Appointments appointment = appointmentOpt.get();
//
//        // 3. Obtener el usuario asociado a la cita
//        Users user = appointment.getStudent();
//        if (user == null) {
//            return Optional.empty(); // No se puede crear el DTO sin usuario
//        }

        // 4. Mapear la información al DTO utilizando el mapper
//        DetailsAppointmentDTO detailsDTO = detailsAppointmentMapper.toDetailsAppointmentDTO(appointment, user);

        // 5. Retornar el DTO dentro de un Optional
//        return Optional.of(detailsDTO);
        return Optional.empty();
    }

    @Override
    public List<Appointments> findAppointmentsByStudentsId(Long studentId) {
        return jpaAppointmentRepository.findByStudentId(studentId).stream().map(appointmentMapper::APPOINTMENTS).toList();
    }

    @Override
    public Page<Appointments> findAllPageable(StatusAppointment status, Pageable pageable) {
        Page<AppointmentsEntity> appointmentsEntities = jpaAppointmentRepository.findByStatusNot(StatusAppointment.DELETED, pageable);

        return appointmentsEntities.map(entity -> appointmentMapper.APPOINTMENTS(entity));
    }

//
//    @Override
//    public Page<Appointments> findAllPageable(StatusAppointment status, Pageable pageable) {
//        // Asumiendo que quieres filtrar por un status Y excluir DELETED
//        Page<AppointmentsEntity> appointmentsEntities;
//
//        if (status != null) {
//            // Filtra por status específico y excluye DELETED
//            appointmentsEntities = jpaAppointmentRepository.findByStatusAndStatusNot(status, StatusAppointment.DELETED, pageable);
//        } else {
//            // Solo excluye DELETED
//            appointmentsEntities = jpaAppointmentRepository.findByStatusNot(StatusAppointment.DELETED, pageable);
//        }
//
//        return appointmentsEntities.map(entity -> appointmentMapper.APPOINTMENTS(entity));
//    }

}
