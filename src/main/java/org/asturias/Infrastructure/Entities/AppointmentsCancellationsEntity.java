package org.asturias.Infrastructure.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "appointment_cancellations")
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentsCancellationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, insertable = false, updatable = false)
    private AppointmentsEntity appointment;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by_user_id", insertable = false, updatable = false)
    private UsersEntity cancelledByUser;

    @Column(name = "cancelled_by_user_id", nullable = false)
    private Long cancelledByUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by_student_id", insertable = false, updatable = false)
    private StudentsEntity cancelledByStudent;

    @Column(name = "cancelled_by_student_id", nullable = false)
    private Long cancelledByStudentId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "cancelled_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime cancelledAt;






}
