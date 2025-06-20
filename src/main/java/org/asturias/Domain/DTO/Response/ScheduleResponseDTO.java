package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {

    private Long id;
    private Integer dayOfWeek;
    private String dayLabel;
    private String startTime;
    private String endTime;
    private Long typeOfAppointmentId;
    private String typeOfAppointmentName;
    private Integer capacity;
    private Integer availableSlots;
    private Integer  appointmentDuration;
    private List<AppointmentBlockDTO> availableBlocks;


}
