package org.asturias.Application.UseCases;

import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;
import org.asturias.Domain.Models.*;
import org.asturias.Domain.Ports.In.RetrieveEntityUseCase;
import org.asturias.Domain.Ports.Out.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RetrieveAppointmentsUseCaseImpl implements RetrieveEntityUseCase {

    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;
    private final ProgramRepositoryPort programRepositoryPort;
    private final TypeOfAppointmentRepositoryPort typeOfAppointmentRepositoryPort;
    private final StudentsRepositoryPort studentsRepositoryPort;
    private final ScheduleRepositoryPort scheduleRepositoryPort;
    private final SearchAppointmentPort searchAppointmentPort;

    public RetrieveAppointmentsUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort, ProgramRepositoryPort programRepositoryPort, TypeOfAppointmentRepositoryPort typeOfAppointmentRepositoryPort, StudentsRepositoryPort studentsRepositoryPort, ScheduleRepositoryPort scheduleRepositoryPort, SearchAppointmentPort searchAppointmentPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
        this.programRepositoryPort = programRepositoryPort;
        this.typeOfAppointmentRepositoryPort = typeOfAppointmentRepositoryPort;
        this.studentsRepositoryPort = studentsRepositoryPort;
        this.scheduleRepositoryPort = scheduleRepositoryPort;
        this.searchAppointmentPort = searchAppointmentPort;
    }


    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepositoryPort.findById(id);
    }

    @Override
    public Optional<Appointments> findAppointmentById(Long id) {
        return  appointmentsRepositoryPort.findById(id);
    }


    @Override
    public List<Appointments> findByDateAppointmentBetween(String start, String end) {
        return appointmentsRepositoryPort.findByDateAppointmentBetween(start, end);
    }

    @Override
    public Optional<DetailsAppointmentDTO> findDetailsAppointmentById(Long id) {
        return appointmentsRepositoryPort.findDetailsAppointmentById(id);
    }

    @Override
    public List<Program> FindAllProgram() {
        return  programRepositoryPort.findAll();

    }

    @Override
    public List<TypeOfAppointment> FindAllTypeAppointment() {
        return typeOfAppointmentRepositoryPort.findAll();
    }

    @Override
    public Optional<Students> getStudentById(Long id) {
        return studentsRepositoryPort.findById(id);
    }

    @Override
    public List<Schedule> FindAllSchedule() {
        return scheduleRepositoryPort.findAll();
    }

    @Override
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepositoryPort.findById(id);
    }

    @Override
    public Optional<Students> getStudentsByEmailAndDocumentNumber(String documentNumber, String email) {
        return studentsRepositoryPort.findByEmailAndNumberDocument(documentNumber, email);
    }

    @Override
    public List<SearchAppointmentsResponseDTO> searchAppointmentsByStudentId(Long studentId) {
        return searchAppointmentPort.findAppointmentsWithStudentInfo(studentId);
    }

    @Override
    public List<SearchAppointmentsResponseDTO> searchAppointmentsByStudentEmailAndDocument(String email, String documentNumber) {
        return searchAppointmentPort.findAppointmentsByStudentEmailAndDocument(email, documentNumber);
    }


}
