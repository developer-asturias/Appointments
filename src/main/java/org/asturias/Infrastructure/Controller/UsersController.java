package org.asturias.Infrastructure.Controller;


import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Domain.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {


    @Autowired
    private AppointmentsService appointmentsService;


    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers() {
        List<Users> users = appointmentsService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    

}
