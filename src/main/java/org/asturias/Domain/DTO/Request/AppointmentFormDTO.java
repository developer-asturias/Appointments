package org.asturias.Domain.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;


public class AppointmentFormDTO {

    private Long userId;

    @NotBlank(message = "El nombre del usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre del usuario no puede superar los 50 caracteres.")
    private String userName;

    @NotBlank(message = "El correo electrónico del usuario no puede estar vacío.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
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

    private Boolean

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public Long getTypeOfAppointmentId() {
        return typeOfAppointmentId;
    }

    public void setTypeOfAppointmentId(Long typeOfAppointmentId) {
        this.typeOfAppointmentId = typeOfAppointmentId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
