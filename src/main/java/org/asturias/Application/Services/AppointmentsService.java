package org.asturias.Application.Services;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.AppointmentDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.*;
import org.asturias.Domain.Ports.In.CreateAppointmentAndUser;
import org.asturias.Domain.Ports.In.CreateEntityUseCase;
import org.asturias.Domain.Ports.In.RetrieveEntityUseCase;
import org.asturias.Domain.Ports.In.UpdateEntityUseCase;
import org.asturias.Infrastructure.Mappers.Request.AppointmentFormDtoMapper;
import org.asturias.Infrastructure.Mappers.Response.CalendarAppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentsService  implements CreateEntityUseCase, RetrieveEntityUseCase, CreateAppointmentAndUser, UpdateEntityUseCase {


    private final CreateEntityUseCase createEntityUseCase;
    private final RetrieveEntityUseCase retrieveEntitiesUseCase;
    private final UpdateEntityUseCase updateEntityUseCase;

    @Autowired
    private AppointmentFormDtoMapper appointmentFormDtoMapper;

    @Autowired
    private CalendarAppointmentMapper  calendarAppointmentMapper;

    public AppointmentsService(CreateEntityUseCase createEntityUseCase, RetrieveEntityUseCase retrieveEntitiesUseCase, UpdateEntityUseCase updateEntityUseCase) {
        this.createEntityUseCase = createEntityUseCase;
        this.retrieveEntitiesUseCase = retrieveEntitiesUseCase;
        this.updateEntityUseCase = updateEntityUseCase;
    }


    @Override
    public Appointments createAppointments(Appointments appointments) {
        return createEntityUseCase.createAppointments(appointments);
    }

    @Override
    public Users createUsers(Users users) {
        return createEntityUseCase.createUsers(users);
    }

    @Override
    public Students createStudents(Students students) {
        return createEntityUseCase.createStudents(students);
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return createEntityUseCase.createSchedule(schedule);
    }


    @Override
    public Optional<Users> getUserById(Long id) {
        return retrieveEntitiesUseCase.getUserById(id);
    }

    @Override
    public Optional<Appointments> findAppointmentById(Long id) {
        return retrieveEntitiesUseCase.findAppointmentById(id);
    }

    @Override
    public List<Appointments> findByDateAppointmentBetween(String start, String end) {
        return retrieveEntitiesUseCase.findByDateAppointmentBetween(start, end);
    }

    @Override
    public Optional<DetailsAppointmentDTO> findDetailsAppointmentById(Long id) {
        return retrieveEntitiesUseCase.findDetailsAppointmentById(id);
    }

    @Override
    public List<Program> FindAllProgram() {
        return retrieveEntitiesUseCase.FindAllProgram();
    }

    @Override
    public List<TypeOfAppointment> FindAllTypeAppointment() {
        return retrieveEntitiesUseCase.FindAllTypeAppointment();
    }

    @Override
    public Optional<Students> getStudentById(Long id) {
        return retrieveEntitiesUseCase.getStudentById(id);
    }

    @Override
    public List<Schedule> FindAllSchedule() {
        return retrieveEntitiesUseCase.FindAllSchedule();
    }

    @Override
    public Optional<Schedule> getScheduleById(Long id) {
        return retrieveEntitiesUseCase.getScheduleById(id);
    }

    @Override
    public Optional<Users> updateUsers(Long id, Users users) {
        return  updateEntityUseCase.updateUsers(id, users);
    }

    @Override
    public Optional<Schedule> updateSchedule(Long id, Schedule schedule) {
        return updateEntityUseCase.updateSchedule(id, schedule);
    }

    @Override
    public Optional<Appointments> updateAppointment(Long id, Appointments appointments) {
        return updateEntityUseCase.updateAppointment(id, appointments);
    }









    @Override
    public void createAppointmentAndUser(AppointmentFormDTO formDTO) {
        // 1. Convertir el DTO en un objeto de dominio Users
        Users user = appointmentFormDtoMapper.mapToUsers(formDTO);
        // 2. Persistir al usuario para obtener el usuario guardado con ID asignado
        Users savedUser = createUsers(user);
        // 3. Convertir el DTO en un objeto de dominio Appointments
        Appointments appointment = appointmentFormDtoMapper.mapToAppointments(formDTO);
        // 4. Asignar el ID del usuario al objeto de dominio Appointments
        appointment.setStudentId(savedUser.getId());
        // 5. Persistir la cita en la base de datos
        createAppointments(appointment);
    }



    @Override
    public CalendarAppointmentDTO getCalendarAppointmentDTO(String start, String end) {
        // 1. Consultar directamente los modelos (si tu método ya hace la conversión)
        List<Appointments> appointmentsModels = findByDateAppointmentBetween(start, end);

        // 2. Validar si la lista está vacía o nula
        if (appointmentsModels == null || appointmentsModels.isEmpty()) {
            return null;
        }
        // 3. Convertir de Appointments (modelo) a AppointmentDTO
        List<AppointmentDTO> appointmentDTOs = appointmentsModels.stream()
                .map(calendarAppointmentMapper::toAppointmentDTO)
                .toList();

        // 4. Agrupar las citas por su estado y contar cuántas hay de cada uno
        Map<StatusAppointment, Long> statuses = appointmentsModels.stream()
                .collect(Collectors.groupingBy(Appointments::getStatus, Collectors.counting()));

        // 5. Construir el DTO completo con la información obtenida
        return calendarAppointmentMapper.toCalendarAppointmentDTO(
                appointmentsModels.size(),
                statuses,
                appointmentDTOs
        );
    }



}
