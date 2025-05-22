package org.asturias.Infrastructure.Controller;


import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Mappers.Response.CalendarAppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<String> createAppointment(@Valid @RequestBody AppointmentFormDTO formDTO) {
        appointmentsService.createAppointmentAndUser(formDTO);
        return ResponseEntity.ok("¡Cita registrada con éxito!");
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




}
