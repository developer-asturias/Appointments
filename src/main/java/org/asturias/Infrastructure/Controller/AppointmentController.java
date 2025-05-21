package org.asturias.Infrastructure.Controller;


import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentsService appointmentsService;



    @PostMapping("/create")
    public ResponseEntity<String> createAppointment(@Valid @RequestBody AppointmentFormDTO formDTO) {
        appointmentsService.createAppointmentAndUser(formDTO);
        return ResponseEntity.ok("¡Cita registrada con éxito!");
    }
}
