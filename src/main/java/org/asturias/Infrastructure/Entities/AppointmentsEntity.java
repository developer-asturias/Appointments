package org.asturias.Infrastructure.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentsEntityListener.class)

public class AppointmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "details", nullable = false, length = 500)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private StudentsEntity students;

    @NotNull
    @Column(name = "student_id", nullable = false, updatable = false)
    private Long studentId;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", insertable = false, updatable = false)
    private ScheduleEntity schedule;

    @Column(name = "update_at")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updateAt;

    @Column(name = "create_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @CreationTimestamp
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", insertable = false, updatable = false)
    private UsersEntity mentors;

    @Column(name = "mentor_id")
    private Long mentorId;

    @Lob
    @Column(name = "mentor_notes")
    private String mentorNotes;


    @NotNull(message = "El enlace de la reunión es obligatorio")
    @Pattern(
            regexp = "^https://teams\\.microsoft\\.com/.*$",
            message = "La URL debe ser un enlace válido de Microsoft Teams"
    )
    @Column(name = "meeting_url", length = 500)
    private String meetingUrl;

    @NotNull
    @Column(name = "start_time", nullable = false, updatable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false, updatable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @Column(name = "status_id", insertable = false, updatable = false)
    private Long statusId;

    @Column(name = "mentor_joined_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime mentorJoinedAt;

    @Column(name = "student_joined_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime studentJoinedAt;

}
