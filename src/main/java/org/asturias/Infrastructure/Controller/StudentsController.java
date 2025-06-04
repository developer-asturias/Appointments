package org.asturias.Infrastructure.Controller;
import lombok.extern.slf4j.Slf4j;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;
import org.asturias.Domain.Models.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentsController {

    @Autowired
    private AppointmentsService appointmentsService;



    @GetMapping("/search-appointments")
    public ResponseEntity<List<SearchAppointmentsResponseDTO>> searchByEmailAndDocument(
            @RequestParam String email,
            @RequestParam String documentNumber) {
        try {
            List<SearchAppointmentsResponseDTO> result = appointmentsService.searchAppointmentsByStudentEmailAndDocument(email, documentNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Registrar el error
            log.error("Error al buscar citas por email y documento: {} - {}", email, documentNumber, e);

            // Devolver una respuesta de error apropiada
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }








}
