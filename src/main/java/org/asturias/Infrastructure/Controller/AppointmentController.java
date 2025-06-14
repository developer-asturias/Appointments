package org.asturias.Infrastructure.Controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.AppointmentsPageableResponseDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {


    @Autowired
    private AppointmentsService appointmentsService;


    // crear una cita
    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentFormDTO formDTO) {
        try {
            ResponseAppointmentDTO responseAppointmentDTO = appointmentsService.createAppointmentAndUser(formDTO);
            return new ResponseEntity<>(responseAppointmentDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear una cita: " + e.getMessage());
        }
    }


    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAppointments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        try {
            // Crear el objeto Sort
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Sort sortBy = Sort.by(sortDirection, sort);

            // Crear el objeto Pageable
            Pageable pageable = PageRequest.of(page, size, sortBy);

            // Obtener las citas paginadas
            Page<AppointmentsPageableResponseDTO> appointmentsPage = appointmentsService.findAllPageable(StatusAppointment.DELETED, pageable);

            return ResponseEntity.ok(appointmentsPage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Parámetros inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las citas: " + e.getMessage());
        }
    }


    // controlador para traer las citas por rango de fechas

    @GetMapping("/by-date-range")
    public ResponseEntity<CalendarAppointmentDTO> getAppointmentsByDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end
    ) {
        // Delegar la lógica al servicio
        CalendarAppointmentDTO responseDTO = appointmentsService.getCalendarAppointmentDTO(start, end);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(responseDTO);
    }


    // controlador de ver a detalle una cita
    @GetMapping(value = "/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentDetails(@PathVariable Long id) {
        Optional<DetailsAppointmentDTO> detailsDTO = appointmentsService.findDetailsAppointmentById(id);

        if (detailsDTO.isPresent()) {
            return ResponseEntity.ok(detailsDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la cita con ID: " + id);
        }
    }










    @PutMapping("/{id}/mentor/{mentorId}")
    public ResponseEntity<?> updateAppointmentMentor(
            @PathVariable("id") Long appointmentId,
            @PathVariable("mentorId") Long mentorId) {

        try {
            return appointmentsService.updateAppointmentMentor(appointmentId, mentorId)
                    .map(ResponseEntity::ok)
                    .orElse(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el mentor: " + e.getMessage());
        }
    }




}
