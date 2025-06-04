package org.asturias.Application.UseCases;

import lombok.RequiredArgsConstructor;

import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.asturias.Domain.Ports.In.SendEmailUseCase;
import org.asturias.Domain.Ports.Out.SendEmailPort;
import org.springframework.stereotype.Component;

@Component
public class SendEmailUseCaseImpl implements SendEmailUseCase {


    private final SendEmailPort sendEmailPort;

    public SendEmailUseCaseImpl(SendEmailPort sendEmailPort) {
        this.sendEmailPort = sendEmailPort;
    }

    @Override
    public void sendAppointmentConfirmation(String to, ResponseAppointmentDTO appointmentDetails) {
        sendEmailPort.sendAppointmentConfirmation(to, appointmentDetails);
    }
}
