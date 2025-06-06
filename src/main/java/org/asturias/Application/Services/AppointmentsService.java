package org.asturias.Application.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asturias.Application.UseCases.CreateAppointmentsUseCaseImpl;
import org.asturias.Application.UseCases.RetrieveAppointmentsUseCaseImpl;
import org.asturias.Application.UseCases.SendEmailUseCaseImpl;
import org.asturias.Application.UseCases.UpdateAppointmentUseCaseImpl;
import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.*;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.*;
import org.asturias.Domain.Ports.In.*;
import org.asturias.Infrastructure.Mappers.Request.AppointmentFormDtoMapper;
import org.asturias.Infrastructure.Mappers.Response.CalendarAppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j

public class AppointmentsService  implements CreateEntityUseCase, RetrieveEntityUseCase, CreateAppointmentAndUser, UpdateEntityUseCase, SendEmailUseCase {


    private final CreateEntityUseCase createEntityUseCase;
    private final RetrieveEntityUseCase retrieveEntitiesUseCase;
    private final UpdateEntityUseCase updateEntityUseCase;
    private final SendEmailUseCase sendEmailUseCase;


    public AppointmentsService(CreateEntityUseCase createEntityUseCase, RetrieveEntityUseCase retrieveEntitiesUseCase, UpdateEntityUseCase updateEntityUseCase, SendEmailUseCase sendEmailUseCase) {
        this.createEntityUseCase = createEntityUseCase;
        this.retrieveEntitiesUseCase = retrieveEntitiesUseCase;
        this.updateEntityUseCase = updateEntityUseCase;
        this.sendEmailUseCase = sendEmailUseCase;
    }

    @Autowired
    private AppointmentFormDtoMapper appointmentFormDtoMapper;

    @Autowired
    private CalendarAppointmentMapper  calendarAppointmentMapper;



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
    public List<Users> getAllUsers() {
        return retrieveEntitiesUseCase.getAllUsers();
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
    public Optional <Students> getStudentsByEmailAndDocumentNumber(String documentNumber, String email) {
        return retrieveEntitiesUseCase.getStudentsByEmailAndDocumentNumber(documentNumber, email);
    }

    @Override
    public Page<AppointmentsPageableResponseDTO> findAllPageable(StatusAppointment status, Pageable pageable) {
        return retrieveEntitiesUseCase.findAllPageable(status, pageable);
    }

    @Override
    public List<SearchAppointmentsResponseDTO> searchAppointmentsByStudentId(Long studentId) {
        return retrieveEntitiesUseCase.searchAppointmentsByStudentId(studentId);
    }

    @Override
    public List<SearchAppointmentsResponseDTO> searchAppointmentsByStudentEmailAndDocument(String email, String documentNumber) {
        return  retrieveEntitiesUseCase.searchAppointmentsByStudentEmailAndDocument(email, documentNumber);
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
    public Optional<Appointments> updateAppointmentMentor(Long id, Long mentorId) {
        return updateEntityUseCase.updateAppointmentMentor(id, mentorId);
    }

    @Override
    public void sendAppointmentConfirmation(String to, ResponseAppointmentDTO appointmentDetails) {
        sendEmailUseCase.sendAppointmentConfirmation(to, appointmentDetails);
    }

















    @Override
    public ResponseAppointmentDTO createAppointmentAndUser(AppointmentFormDTO formDTO) {
        // 1. Obtener o crear estudiante usando Optional
        Students student = getStudentsByEmailAndDocumentNumber(
                formDTO.getUserEmail(),
                formDTO.getNumberDocument())
                .map(existingStudent -> {
                    log.info("Estudiante encontrado con ID: {}", existingStudent.getId());
                    return existingStudent;
                })
                .orElseGet(() -> {
                    log.info("No se encontró estudiante con email: {} y documento: {}. Creando nuevo estudiante.",
                            formDTO.getUserEmail(), formDTO.getNumberDocument());
                    Students newStudent = appointmentFormDtoMapper.mapToUsers(formDTO);
                    Students createdStudent = createStudents(newStudent);
                    log.info("Nuevo estudiante creado con ID: {}", createdStudent.getId());
                    return createdStudent;
                });

        // 2. Convertir el DTO en un objeto de dominio Appointments
        Appointments appointment = appointmentFormDtoMapper.mapToAppointments(formDTO);

        // 3. Asignar el ID del estudiante al objeto de dominio Appointments
        appointment.setStudentId(student.getId());

        // 4. Persistir la cita en la base de datos
        Appointments savedAppointment = createAppointments(appointment);

        // 5. Crear y devolver el DTO de respuesta
        ResponseAppointmentDTO responseAppointmentDTO = new ResponseAppointmentDTO();
        responseAppointmentDTO.setNameStudent(student.getName());
        responseAppointmentDTO.setDate(savedAppointment.getDateAppointment());
        responseAppointmentDTO.setUserEmail(student.getEmail());

        // enviar correo
        sendAppointmentConfirmation(student.getEmail(), responseAppointmentDTO);

        return responseAppointmentDTO;
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
