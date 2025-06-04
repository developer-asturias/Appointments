package org.asturias.Infrastructure.Adapters;


import org.asturias.Application.Email.MailBuilder;
import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.asturias.Domain.Ports.Out.SendEmailPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class SendEmailAdapter implements SendEmailPort {

    private final JavaMailSender emailSender;
    private final MailBuilder mailBuilder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public SendEmailAdapter(JavaMailSender emailSender, MailBuilder mailBuilder) {
        this.emailSender = emailSender;
        this.mailBuilder = mailBuilder;
    }

    @Override
    public void sendAppointmentConfirmation(String to, ResponseAppointmentDTO appointmentDetails) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setSubject("Confirmación de Cita - Corporación Universitaria Asturias");

            // Add HTML content
            messageHelper.setText(mailBuilder.buildTemplate(appointmentDetails.getDate(), appointmentDetails.getNameStudent()), true);

            // Add the logo as an inline attachment
            ClassPathResource logoResource = new ClassPathResource("static/img/asturias-logo.png");
            messageHelper.addInline("asturias-logo", logoResource);
        };

        try {
            emailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            throw new MailPreparationException("Failed to send appointment confirmation email to " + to, e);
        }
    }
}
