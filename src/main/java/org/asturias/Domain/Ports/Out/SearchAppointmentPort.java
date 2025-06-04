package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;

import java.util.List;

public interface SearchAppointmentPort {

    List<SearchAppointmentsResponseDTO> findAppointmentsWithStudentInfo(Long studentId);
    List<SearchAppointmentsResponseDTO> findAppointmentsByStudentEmailAndDocument(String email, String documentNumber);

}
