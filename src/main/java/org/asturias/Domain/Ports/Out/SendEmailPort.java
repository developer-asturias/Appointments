package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;

public interface SendEmailPort {

    void sendAppointmentConfirmation(String to, ResponseAppointmentDTO appointmentDetails);
}
