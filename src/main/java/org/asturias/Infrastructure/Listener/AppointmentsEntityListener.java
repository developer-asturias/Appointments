package org.asturias.Infrastructure.Listener;

import jakarta.persistence.PrePersist;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;

public class AppointmentsEntityListener {

//    @PrePersist
//    public void prePersist(AppointmentsEntity entity) {
//        if (entity.getStatus() == null) {
//            entity.setStatus(StatusAppointment.PENDING);
//        }
//    }


}
