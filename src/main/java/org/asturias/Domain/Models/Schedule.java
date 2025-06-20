package org.asturias.Domain.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Domain.Models.ValueObjects.TimeRange;
import org.asturias.Infrastructure.Entities.TypeOfAppointmentEntity;

import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    private Long id;

    private Boolean isActive;

    @Min(value = 1, message = "El día de la semana debe ser mínimo 1 (Lunes)")
    @Max(value = 6, message = "El día de la semana debe ser máximo 6 (Sábado)")
    private Integer dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;


    @NotNull(message = "El ID del tipo de cita no puede ser nulo")
    private Long typeOfAppointmentId;

    @JsonIgnore
    private TypeOfAppointmentEntity typeOfAppointment;

    @Min(value = 1, message = "El número mínimo de cupos debe ser 1")
    private Integer capacity;



    public Schedule(int dayOfWeek, LocalTime startTime, LocalTime endTime, Long typeOfAppointmentId) {
        if (dayOfWeek < 1 || dayOfWeek > 6) {
            throw new IllegalArgumentException("Día de la semana inválido");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Los tiempos no pueden ser nulos");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la de inicio");
        }
        if (typeOfAppointmentId == null) {
            throw new IllegalArgumentException("El ID de tipo de cita no puede ser nulo");
        }

        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.typeOfAppointmentId = typeOfAppointmentId;
        this.isActive = true;
        this.capacity = 1;
    }

    // Getters
    public TimeRange getTimeRange() {
        return new TimeRange(startTime, endTime);
    }

    public boolean conflictsWith(Schedule other) {
        if (this.dayOfWeek != other.dayOfWeek ||
                !this.typeOfAppointmentId.equals(other.typeOfAppointmentId)) {
            return false;
        }
        return this.getTimeRange().overlapsWith(other.getTimeRange());
    }

    public boolean isDuplicateOf(Schedule other) {
        return this.dayOfWeek == other.dayOfWeek &&
                this.typeOfAppointmentId.equals(other.typeOfAppointmentId) &&
                this.getTimeRange().equalsExactly(other.getTimeRange());
    }
}
