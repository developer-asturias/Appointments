package org.asturias.Infrastructure.Adapters;

import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Ports.Out.ScheduleRepositoryPort;
import org.asturias.Infrastructure.Entities.ScheduleEntity;
import org.asturias.Infrastructure.Mappers.Entities.ScheduleMapper;
import org.asturias.Infrastructure.Repositories.JpaScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class JpaScheduleRepositoryAdapter  implements ScheduleRepositoryPort {


    @Autowired
    private JpaScheduleRepository jpaScheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    public JpaScheduleRepositoryAdapter(JpaScheduleRepository jpaScheduleRepository, ScheduleMapper scheduleMapper) {
        this.jpaScheduleRepository = jpaScheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleEntity scheduleEntity = scheduleMapper.SCHEDULE_ENTITY(schedule);
        return scheduleMapper.SCHEDULE(jpaScheduleRepository.save(scheduleEntity));
    }



    @Override
    public Optional<Schedule> update(Long id, Schedule schedule) {
        return jpaScheduleRepository.findById(id)
                .map(existing -> {
                    ScheduleEntity entity = scheduleMapper.SCHEDULE_ENTITY(schedule);
                    entity.setId(id);
                    ScheduleEntity updated = jpaScheduleRepository.save(entity);
                    return Optional.of(scheduleMapper.SCHEDULE(updated));
                })
                .orElse(Optional.empty());
    }


    @Override
    public Optional<Schedule> findById(Long id) {
        return jpaScheduleRepository.findById(id).map(scheduleEntity -> scheduleMapper.SCHEDULE(scheduleEntity));
    }

    @Override
    public List<Schedule> findAll() {
        List<ScheduleEntity> scheduleEntity = jpaScheduleRepository.findAll();
        return scheduleMapper.SCHEDULE_LIST(scheduleEntity);
    }

    @Override
    public List<Schedule> findByDayAndType(int dayOfWeek, Long typeOfAppointmentId) {
        return jpaScheduleRepository
                .findByDayOfWeekAndTypeOfAppointmentId(dayOfWeek, typeOfAppointmentId)
                .stream()
                .map(scheduleMapper::SCHEDULE)
                .collect(Collectors.toList());
    }



}
