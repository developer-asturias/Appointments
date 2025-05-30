package org.asturias.Infrastructure.Controller;


import jakarta.validation.Valid;
import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.Models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {


    @Autowired
    private AppointmentsService appointmentsService;




    @PostMapping(value = "/create", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<?> createSchedule(@Valid @RequestBody Schedule schedule ) {
        try {
            Schedule createdSchedule = appointmentsService.createSchedule(schedule);
            return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al establecer un horario: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllSchedule(){
        try {
            List<Schedule>  scheduleList =  appointmentsService.FindAllSchedule();
            return  new ResponseEntity<>(scheduleList, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener todos los horarios: " + e.getMessage());
        }


    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable Long id ){
        Optional <Schedule>  schedule = appointmentsService.getScheduleById(id);

        if(schedule.isPresent()){
            return new ResponseEntity<>(schedule, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update-schedule/{id}")
    public ResponseEntity<?> updateScheduleById(@PathVariable Long id, @Valid @RequestBody Schedule schedule) {
        try {
            // Verificar si existe el horario
            Optional<Schedule> existingSchedule = appointmentsService.getScheduleById(id);

            if (existingSchedule.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Horario no encontrado con id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Intentar actualizar
            Optional<Schedule> updatedSchedule = appointmentsService.updateSchedule(id, schedule);

            if (updatedSchedule.isPresent()) {
                return ResponseEntity.ok(updatedSchedule.get());
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se pudo actualizar el horario");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al actualizar el horario: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }








}
