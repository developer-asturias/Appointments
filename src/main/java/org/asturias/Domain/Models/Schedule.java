package org.asturias.Domain.Models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Infrastructure.Entities.TypeOfAppointmentEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    private Long id;

    private Boolean isActive;

    @Min(value = 1, message = "El día de la semana debe ser mínimo 1 (Lunes)")
    @Max(value = 6, message = "El día de la semana debe ser máximo 6 (Sábado)")
    private Integer dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long typeOfAppointmentId;

    @JsonIgnore
    private TypeOfAppointmentEntity typeOfAppointment;


    @Min(value = 1, message = "El número mínimo de cupos debe ser 1")
    private Integer capacity;




}
