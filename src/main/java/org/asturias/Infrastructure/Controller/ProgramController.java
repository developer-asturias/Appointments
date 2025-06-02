package org.asturias.Infrastructure.Controller;


import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/program")
public class ProgramController {


    @Autowired
    private AppointmentsService appointmentsService;


    @GetMapping("/get-all")
    public ResponseEntity<?> getAllSchedule() {
        try {
            List<Program> programs = appointmentsService.FindAllProgram();
            return new ResponseEntity<>(programs, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener todos los programas : " + e.getMessage());
        }
    }

}