package org.asturias.Infrastructure.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Infrastructure.Listener.AppointmentsEntityListener;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;



@Entity
@Table(name = "Appointment")
@EntityListeners(AppointmentsEntityListener.class)
public class AppointmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "la fecha de la cita no deber ser nula")
    @Column(name = "datetime_appointment", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime dateAppointment;

    // Relación con UsersEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UsersEntity users;

    @NotNull
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private StatusAppointment status;

    @Column(name = "create_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updateAt;

    @Column(name = "date_remove")
    private LocalDateTime dateRemove;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_appointment_id", insertable = false, updatable = false)
    private TypeOfAppointmentEntity typeOfAppointment;

    @Column(name = "type_appointment_id", nullable = false)
    private Long typeOfAppointmentId;

    @Column(name = "details", nullable = false, length = 500)
    private String Details;


    public AppointmentsEntity(Long id, String name, LocalDateTime dateAppointment, UsersEntity users, Long userId, StatusAppointment status, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime dateRemove, TypeOfAppointmentEntity typeOfAppointment, Long typeOfAppointmentId, String details) {
        this.id = id;
        this.name = name;
        this.dateAppointment = dateAppointment;
        this.users = users;
        this.userId = userId;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.dateRemove = dateRemove;
        this.typeOfAppointment = typeOfAppointment;
        this.typeOfAppointmentId = typeOfAppointmentId;
        Details = details;
    }

    public AppointmentsEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateAppointment() {
        return dateAppointment;
    }

    public void setDateAppointment(LocalDateTime dateAppointment) {
        this.dateAppointment = dateAppointment;
    }

    public UsersEntity getUsers() {
        return users;
    }

    public void setUsers(UsersEntity users) {
        this.users = users;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public StatusAppointment getStatus() {
        return status;
    }

    public void setStatus(StatusAppointment status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDateTime getDateRemove() {
        return dateRemove;
    }

    public void setDateRemove(LocalDateTime dateRemove) {
        this.dateRemove = dateRemove;
    }

    public TypeOfAppointmentEntity getTypeOfAppointment() {
        return typeOfAppointment;
    }

    public void setTypeOfAppointment(TypeOfAppointmentEntity typeOfAppointment) {
        this.typeOfAppointment = typeOfAppointment;
    }

    public Long getTypeOfAppointmentId() {
        return typeOfAppointmentId;
    }

    public void setTypeOfAppointmentId(Long typeOfAppointmentId) {
        this.typeOfAppointmentId = typeOfAppointmentId;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
