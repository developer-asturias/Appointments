package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;




public class DetailsAppointmentDTO {


    private String appointmentName;

    private String appointmentDescription;

    private String appointmentDate;

    private String programName;

    private String typeAppointment;

    private StatusAppointment appointmentStatus;

    private String numberDocument;

    private String linkTeams;
    private String nameStudent;

    private String phone;

    private String email;


    public DetailsAppointmentDTO(String appointmentName, String appointmentDescription, String appointmentDate, String programName, String typeAppointment, StatusAppointment appointmentStatus, String numberDocument, String linkTeams, String nameStudent, String phone, String email) {
        this.appointmentName = appointmentName;
        this.appointmentDescription = appointmentDescription;
        this.appointmentDate = appointmentDate;
        this.programName = programName;
        this.typeAppointment = typeAppointment;
        this.appointmentStatus = appointmentStatus;
        this.numberDocument = numberDocument;
        this.linkTeams = linkTeams;
        this.nameStudent = nameStudent;
        this.phone = phone;
        this.email = email;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTypeAppointment() {
        return typeAppointment;
    }

    public void setTypeAppointment(String typeAppointment) {
        this.typeAppointment = typeAppointment;
    }

    public StatusAppointment getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(StatusAppointment appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public String getLinkTeams() {
        return linkTeams;
    }

    public void setLinkTeams(String linkTeams) {
        this.linkTeams = linkTeams;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
