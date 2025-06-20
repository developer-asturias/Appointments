package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBlockDTO  {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableSlot;
    private Integer booked;
    private Boolean isFull;
}
