package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.asturias.Domain.Models.Appointments;

public interface SendEmailUseCase {
    void sendAppointmentConfirmation(String to, ResponseAppointmentDTO appointmentDetails);


}
