package org.asturias.Infrastructure.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Infrastructure.Listener.AppointmentsEntityListener;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;



@Setter
@Getter
@Entity
@Table(name = "Appointment")
@EntityListeners(AppointmentsEntityListener.class)

public class AppointmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "la fecha de la cita no deber ser nula")
    @Column(name = "datetime_appointment", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime dateAppointment;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UsersEntity user;


    @Column(name = "user_id")
    private Long userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private StudentsEntity students;

    @NotNull
    @Column(name = "student_id", nullable = false, updatable = false)
    private Long studentId;

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


    public AppointmentsEntity(Long id, LocalDateTime dateAppointment, StudentsEntity students, Long studentId, StatusAppointment status, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime dateRemove, TypeOfAppointmentEntity typeOfAppointment, Long typeOfAppointmentId, String details) {
        this.id = id;
        this.dateAppointment = dateAppointment;
        this.students = students;
        this.studentId = studentId;
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


}
