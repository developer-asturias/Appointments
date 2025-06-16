package org.asturias.Domain.Models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Infrastructure.Entities.ScheduleEntity;
import org.asturias.Infrastructure.Entities.StatusEntity;
import org.asturias.Infrastructure.Entities.StudentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Appointments {

    private Long id;

    private String details;

    private StudentsEntity students;

    private Schedule schedule;

    private LocalDateTime updateAt;

    private LocalDateTime createAt;

    private UsersEntity mentors;

    private String mentorNotes;

    private String meetingUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private StatusEntity status;

    private LocalDateTime mentorJoinedAt;

    private LocalDateTime studentJoinedAt;


}
