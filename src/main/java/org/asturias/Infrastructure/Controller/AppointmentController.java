package org.asturias.Infrastructure.Controller;


import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.*;
import org.asturias.Infrastructure.Mappers.Response.CalendarAppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {


    @Autowired
    private AppointmentsService appointmentsService;


    // crear una cita
    @PostMapping(value = "/create", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentFormDTO formDTO ) {
        try {
            ResponseAppointmentDTO responseAppointmentDTO = appointmentsService.createAppointmentAndUser(formDTO);
            return new ResponseEntity<>(responseAppointmentDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear una cita: " + e.getMessage());
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




}
