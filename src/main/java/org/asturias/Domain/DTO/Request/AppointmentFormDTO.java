package org.asturias.Domain.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Infrastructure.Validation.AsturiasEmail;


@Setter
@Getter
public class AppointmentFormDTO {

    private Long userId;

    @NotBlank(message = "El nombre del usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre del usuario no puede superar los 50 caracteres.")
    private String userName;

    @NotBlank(message = "El correo electrónico del usuario no puede estar vacío.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    @AsturiasEmail(message = "El correo electrónico debe pertenecer al dominio @asturias.edu.co")
    private String userEmail;


    @NotBlank(message = "El número de teléfono del usuario no puede estar vacío.")
    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres.")
    private String phone;

    private Long programId;

    @NotBlank(message = "El número de documento no puede estar vacío.")
    private String numberDocument;

    @NotNull(message = "La fecha de la cita no puede ser nula.")
    private LocalDateTime date;

    @NotNull(message = "El tipo de cita no puede ser nulo.")
    private Long typeOfAppointmentId;

    @Size(max = 200, message = "Los detalles no pueden superar los 200 caracteres.")
    private String details;

    private Boolean dataTreatments;


    public AppointmentFormDTO() {
    }


    public AppointmentFormDTO(Long userId, String userName, String userEmail, String phone, Long programId, String numberDocument, LocalDateTime date, Long typeOfAppointmentId, String details) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.phone = phone;
        this.programId = programId;
        this.numberDocument = numberDocument;
        this.date = date;
        this.typeOfAppointmentId = typeOfAppointmentId;
        this.details = details;
    }


}
