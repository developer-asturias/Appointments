package org.asturias.Infrastructure.Adapters;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.asturias.Domain.DTO.Response.AppointmentsPageableResponseDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.asturias.Infrastructure.Mappers.Entities.AppointmentMapper;
import org.asturias.Infrastructure.Mappers.Response.AppointmentsPageableResponseMapper;
import org.asturias.Infrastructure.Mappers.Response.DetailsAppointmentMapper;
import org.asturias.Infrastructure.Repositories.JpaAppointmentRepository;
import org.asturias.Infrastructure.Repositories.JpaUsersRepository;
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
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private AppointmentMapper   appointmentMapper;

    @Autowired
    private DetailsAppointmentMapper detailsAppointmentMapper;

    @Autowired
    private AppointmentsPageableResponseMapper appointmentsPageableResponseMapper;



    public JpaAppointmentRepositoryAdapter(JpaAppointmentRepository jpaAppointmentRepository, AppointmentMapper appointmentMapper) {
        this.jpaAppointmentRepository = jpaAppointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public Optional<Appointments> update(Appointments appointments) {
//        if (jpaAppointmentRepository.existsById(appointments.getId())){
//            AppointmentsEntity appointmentsEntity = appointmentMapper.APPOINTMENTS_ENTITY(appointments);
//            AppointmentsEntity updatedAppointmemt= jpaAppointmentRepository.save(appointmentsEntity);
//            return Optional.of(appointmentMapper.APPOINTMENTS(updatedAppointmemt));
//        }
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
//        return jpaAppointmentRepository.findByDateAppointmentBetween(startDate, endDate)
//                .stream().map(appointmentMapper::APPOINTMENTS)
//                .toList();
    return  null;
    }



    @Override
    public Optional<DetailsAppointmentDTO> findDetailsAppointmentById(Long id) {
        Optional<Appointments> appointmentOpt = findById(id);
        if (appointmentOpt.isPresent()) {
            Appointments appointment = appointmentOpt.get();
            DetailsAppointmentDTO detailsDTO = detailsAppointmentMapper.toDetailsAppointmentDTO(appointment);
            return Optional.of(detailsDTO);
        } else {
            return Optional.empty();
        }
    }




    @Override
    public List<Appointments> findAppointmentsByStudentsId(Long studentId) {
        return jpaAppointmentRepository.findByStudentId(studentId).stream().map(appointmentMapper::APPOINTMENTS).toList();
    }




    @Override
    public Page<AppointmentsPageableResponseDTO> findAllPageable(StatusAppointment status, Pageable pageable) {
        Page<AppointmentsEntity> appointmentsEntities = jpaAppointmentRepository.findByStatusNot(StatusAppointment.DELETED, pageable);
        Page<Appointments> appointmentsPage = appointmentsEntities.map(entity -> appointmentMapper.APPOINTMENTS(entity));
        return appointmentsPage.map(entity -> appointmentsPageableResponseMapper.toDto(entity));
    }



    @Override
    @Transactional
    public Optional<Appointments> updateAppointmentMentor(Long id, Long mentorId) {

        Optional<AppointmentsEntity> appointmentOpt = jpaAppointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            throw new EntityNotFoundException("La cita con id " + id + " no existe");
        }
        if (!jpaUsersRepository.existsById(mentorId)) {
            throw new EntityNotFoundException("El mentor con id " + mentorId + " no existe");
        }
        AppointmentsEntity appointmentEntity = appointmentOpt.get();
//        appointmentEntity.setUserId(mentorId);
//        appointmentEntity.setStatus(StatusAppointment.ASSIGNED);
        AppointmentsEntity updatedEntity = jpaAppointmentRepository.save(appointmentEntity);
        return Optional.of(appointmentMapper.APPOINTMENTS(updatedEntity));
    }


}
