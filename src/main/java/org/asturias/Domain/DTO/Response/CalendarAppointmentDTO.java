package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;

import java.util.List;
import java.util.Map;



public class CalendarAppointmentDTO {

    private int totalAppointments;
    private Map<StatusAppointment, Long> statuses;

    private List<AppointmentDTO> appointments;


    public CalendarAppointmentDTO(int totalAppointments, Map<StatusAppointment, Long> statuses, List<AppointmentDTO> appointments) {
        this.totalAppointments = totalAppointments;
        this.statuses = statuses;
        this.appointments = appointments;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Map<StatusAppointment, Long> getStatuses() {
        return statuses;
    }

    public void setStatuses(Map<StatusAppointment, Long> statuses) {
        this.statuses = statuses;
    }

    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDTO> appointments) {
        this.appointments = appointments;
    }


}
