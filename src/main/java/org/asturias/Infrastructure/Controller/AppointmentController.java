package org.asturias.Infrastructure.Controller;


import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.TypeOfAppointment;
import org.asturias.Domain.Models.Users;
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


    private final CalendarAppointmentMapper calendarAppointmentMapper;

    @Autowired
    public AppointmentController(CalendarAppointmentMapper calendarAppointmentMapper) {
        this.calendarAppointmentMapper = calendarAppointmentMapper;
    }

    @Autowired
    private AppointmentsService appointmentsService;


    // crear una cita
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createAppointment(@Valid @RequestBody AppointmentFormDTO formDTO) {
        appointmentsService.createAppointmentAndUser(formDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "¡Cita registrada con éxito!");
        return ResponseEntity.ok(response);
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


    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAppointmentTypes() {
        try {
            List<TypeOfAppointment> appointmentTypes = appointmentsService.FindAllTypeAppointment();

            if (appointmentTypes == null || appointmentTypes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No se encontraron tipos de citas registrados");
            }

            return ResponseEntity.ok(appointmentTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los tipos de citas: " + e.getMessage());
        }
    }


    @GetMapping(value = "/programs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPrograms() {
        try {
            List<Program> programs = appointmentsService.FindAllProgram();

            if (programs == null || programs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No se encontraron programas registrados");
            }

            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los programas: " + e.getMessage());
        }
    }



}
