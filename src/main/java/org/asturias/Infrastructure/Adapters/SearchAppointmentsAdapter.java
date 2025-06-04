package org.asturias.Infrastructure.Adapters;

import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.SearchAppointmentPort;
import org.asturias.Domain.Ports.Out.StudentsRepositoryPort;
import org.asturias.Infrastructure.Mappers.Response.SearchAppointmentsMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchAppointmentsAdapter implements SearchAppointmentPort {

    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final StudentsRepositoryPort studentsRepositoryPort;
    private final SearchAppointmentsMapper searchAppointmentsMapper;



    public SearchAppointmentsAdapter(AppointmentsRepositoryPort appointmentsRepositoryPort, StudentsRepositoryPort studentsRepositoryPort, SearchAppointmentsMapper searchAppointmentsMapper) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.studentsRepositoryPort = studentsRepositoryPort;
        this.searchAppointmentsMapper = searchAppointmentsMapper;
    }

    @Override
    public List<SearchAppointmentsResponseDTO> findAppointmentsWithStudentInfo(Long studentId) {
        List<Appointments> appointments = appointmentsRepositoryPort.findAppointmentsByStudentsId(studentId);
        Optional<Students> studentOpt = studentsRepositoryPort.findById(studentId);

        if (studentOpt.isEmpty() || appointments.isEmpty()) {
            return new ArrayList<>();
        }

        Students student = studentOpt.get();

        return appointments.stream()
                .map(appointment -> searchAppointmentsMapper.toDto(appointment, student))
                .collect(Collectors.toList());
    }



    @Override
    public List<SearchAppointmentsResponseDTO> findAppointmentsByStudentEmailAndDocument(String email, String documentNumber) {
        Optional <Students> students = studentsRepositoryPort.findByEmailAndNumberDocument(email, documentNumber);
        if (students.isPresent()) {
            return findAppointmentsWithStudentInfo(students.get().getId());
        } else {
            return new ArrayList<>();
        }
    }



}

