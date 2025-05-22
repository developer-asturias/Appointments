/**
 * Sistema de Citas - Implementación con JavaScript puro
 */

// Referencias a elementos del DOM
const studentView = document.getElementById('student-view');
const adminView = document.getElementById('admin-view');
const btnStudentView = document.getElementById('btn-student-view');
const btnAdminView = document.getElementById('btn-admin-view');
const appointmentForm = document.getElementById('appointment-form');
const appointmentsContainer = document.getElementById('appointments-container');
const cancelModal = document.getElementById('cancel-modal');
const btnCancelBack = document.getElementById('btn-cancel-back');
const btnConfirmCancel = document.getElementById('btn-confirm-cancel');
const successNotification = document.getElementById('success-notification');
const notificationMessage = document.getElementById('notification-message');
const btnCloseNotification = document.getElementById('btn-close-notification');
const searchDocument = document.getElementById('search-document');
const btnSearch = document.getElementById('btn-search');
const upcomingAppointments = document.getElementById('upcoming-appointments');
const calendarBody = document.getElementById('calendar-body');
const currentMonthYear = document.getElementById('current-month-year');
const btnPrevMonth = document.getElementById('btn-prev-month');
const btnNextMonth = document.getElementById('btn-next-month');
const btnToday = document.getElementById('btn-today');
const btnMonthView = document.getElementById('btn-month-view');
const btnWeekView = document.getElementById('btn-week-view');
const btnDayView = document.getElementById('btn-day-view');
const appointmentDetailsModal = document.getElementById('appointment-details-modal');
const appointmentDetailsContent = document.getElementById('appointment-details-content');
const btnCloseDetails = document.getElementById('btn-close-details');
const btnCloseDetailsFooter = document.getElementById('btn-close-details-footer');
const statusSelect = document.getElementById('status-select');
const btnUpdateStatus = document.getElementById('btn-update-status');
const teamsLinkDisplay = document.getElementById('teams-link-display');
const teamsLinkEdit = document.getElementById('teams-link-edit');
const teamsLinkInput = document.getElementById('teams-link-input');
const btnEditTeamsLink = document.getElementById('btn-edit-teams-link');
const btnCancelTeamsLink = document.getElementById('btn-cancel-teams-link');
const btnSaveTeamsLink = document.getElementById('btn-save-teams-link');

// Variables globales
let selectedAppointmentId = null;
let currentDate = new Date();
let userEmail = 'maria.gonzalez@example.com'; // Email de ejemplo para el usuario actual
let appointmentTypes = [];
let programs = [];
let selectedAppointment = null;
let calendarView = 'month';

// Inicialización
document.addEventListener('DOMContentLoaded', init);

/**
 * Inicializar la aplicación
 */
async function init() {
    // Cargar datos iniciales
    await Promise.all([
        loadAppointmentTypes(),
        loadPrograms()
    ]);

    // Configurar manejadores de eventos
    setupEventListeners();

    // Mostrar vista por defecto (estudiante)
    switchView('student');

    // Cargar citas del usuario actual
    await loadUserAppointments();
}

/**
 * Cargar tipos de citas
 */
async function loadAppointmentTypes() {
    try {
        appointmentTypes = await API.getAppointmentTypes();
        const typeSelect = document.getElementById('typeOfAppointment');

        // Limpiar opciones existentes excepto la primera
        while (typeSelect.options.length > 1) {
            typeSelect.remove(1);
        }

        // Agregar opciones
        appointmentTypes.forEach(type => {
            const option = document.createElement('option');
            option.value = type.id;
            option.textContent = type.name;
            typeSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar tipos de citas:', error);
        showNotification('Error al cargar tipos de citas. Por favor, recargue la página.', 'error');
    }
}

/**
 * Cargar programas académicos
 */
async function loadPrograms() {
    try {
        programs = await API.getPrograms();
        const programSelect = document.getElementById('programId');

        // Limpiar opciones existentes excepto la primera
        while (programSelect.options.length > 1) {
            programSelect.remove(1);
        }

        // Agregar opciones
        programs.forEach(program => {
            const option = document.createElement('option');
            option.value = program.id;
            option.textContent = program.name;
            programSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar programas académicos:', error);
        showNotification('Error al cargar programas académicos. Por favor, recargue la página.', 'error');
    }
}

/**
 * Configurar listeners de eventos
 */
function setupEventListeners() {
    // Cambio de vistas
    btnStudentView.addEventListener('click', () => switchView('student'));
    btnAdminView.addEventListener('click', () => switchView('admin'));

    // Formulario de creación de citas
    appointmentForm.addEventListener('submit', handleAppointmentSubmit);

    // Buscar por cédula
    btnSearch.addEventListener('click', handleDocumentSearch);
    searchDocument.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            handleDocumentSearch();
        }
    });

    // Modal de cancelación
    btnCancelBack.addEventListener('click', () => {
        cancelModal.classList.remove('active');
    });

    btnConfirmCancel.addEventListener('click', handleCancelConfirmation);

    // Notificación
    btnCloseNotification.addEventListener('click', () => {
        successNotification.classList.remove('active');
    });

    // Controles del calendario
    btnPrevMonth.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar();
    });

    btnNextMonth.addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar();
    });

    btnToday.addEventListener('click', () => {
        currentDate = new Date();
        renderCalendar();
    });

    // Vistas del calendario
    btnMonthView.addEventListener('click', () => setCalendarView('month'));
    btnWeekView.addEventListener('click', () => setCalendarView('week'));
    btnDayView.addEventListener('click', () => setCalendarView('day'));

    // Modal de detalles de cita
    btnCloseDetails.addEventListener('click', closeAppointmentDetails);
    btnCloseDetailsFooter.addEventListener('click', closeAppointmentDetails);

    // Actualizar estado de cita
    btnUpdateStatus.addEventListener('click', handleStatusUpdate);

    // Manejo del enlace de Teams
    btnEditTeamsLink.addEventListener('click', showTeamsLinkEdit);
    btnCancelTeamsLink.addEventListener('click', hideTeamsLinkEdit);
    btnSaveTeamsLink.addEventListener('click', saveTeamsLink);
}

/**
 * Cambiar entre las vistas de la aplicación
 * @param {string} view - Nombre de la vista (student o admin)
 */
function switchView(view) {
    // Ocultar todas las vistas
    studentView.classList.add('hidden');
    adminView.classList.add('hidden');

    // Desactivar todos los botones de navegación
    btnStudentView.classList.remove('active');
    btnAdminView.classList.remove('active');

    // Mostrar la vista seleccionada
    if (view === 'student') {
        studentView.classList.remove('hidden');
        btnStudentView.classList.add('active');
        loadUserAppointments();
    } else if (view === 'admin') {
        adminView.classList.remove('hidden');
        btnAdminView.classList.add('active');
        renderCalendar();
        loadUpcomingAppointments();
    }
}

/**
 * Manejar el envío del formulario de citas
 * @param {Event} event - Evento submit
 */
async function handleAppointmentSubmit(event) {
    event.preventDefault();

    // Recopilar datos del formulario
    const formData = new FormData(appointmentForm);
    const appointmentData = {
        userName: formData.get('userName'),
        userEmail: formData.get('userEmail'),
        phone: formData.get('phone'),
        programId: parseInt(formData.get('programId')),
        numberDocument: formData.get('numberDocument'),
        date: new Date(formData.get('date')).toISOString(),
        appointmentName: formData.get('appointmentName'),
        typeOfAppointment: parseInt(formData.get('typeOfAppointment')),
        details: formData.get('details') || null,
        status: 'scheduled',
        teamsLink: null // Inicialmente sin enlace de Teams
    };

    try {
        // Enviar datos al servidor
        await API.createAppointment(appointmentData);

        // Limpiar formulario
        appointmentForm.reset();

        // Mostrar notificación
        showNotification('La cita ha sido agendada exitosamente.');

        // Recargar citas del usuario
        loadUserAppointments();
    } catch (error) {
        console.error('Error al crear la cita:', error);
        showNotification('Error al agendar la cita. Por favor, intente nuevamente.', 'error');
    }
}

/**
 * Cargar citas del usuario actual
 */
async function loadUserAppointments() {
    try {
        const appointments = await API.getAppointmentsByEmail(userEmail);

        // Limpiar contenedor de citas
        appointmentsContainer.innerHTML = '';

        if (appointments.length === 0) {
            appointmentsContainer.innerHTML = '<div class="empty-appointments">No tienes citas agendadas.</div>';
            return;
        }

        // Ordenar citas por fecha (primero las más recientes)
        appointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Renderizar cada cita
        appointments.forEach(appointment => {
            const appointmentEl = createAppointmentElement(appointment);
            appointmentsContainer.appendChild(appointmentEl);
        });
    } catch (error) {
        console.error('Error al cargar citas:', error);
        appointmentsContainer.innerHTML = '<div class="empty-appointments">Error al cargar las citas. Por favor, intente nuevamente.</div>';
    }
}

/**
 * Crear elemento HTML para una cita
 * @param {Object} appointment - Datos de la cita
 * @returns {HTMLElement} - Elemento de la cita
 */
function createAppointmentElement(appointment) {
    const appointmentEl = document.createElement('div');
    appointmentEl.className = 'appointment-item';

    // Formato de fecha y hora
    const appointmentDate = new Date(appointment.date);
    const formattedDate = appointmentDate.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });

    const formattedTime = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // Obtener nombre del tipo de cita
    const appointmentTypeName = getAppointmentTypeName(appointment.typeOfAppointment);

    // Clase de badge según estado
    let badgeClass = 'badge-scheduled';
    let badgeText = 'Programada';

    if (appointment.status === 'completed') {
        badgeClass = 'badge-completed';
        badgeText = 'Completada';
    } else if (appointment.status === 'cancelled') {
        badgeClass = 'badge-cancelled';
        badgeText = 'Cancelada';
    }

    // HTML de la cita
    appointmentEl.innerHTML = `
        <div class="header">
            <div>
                <h3 class="title">${appointment.appointmentName}</h3>
                <p class="type">${appointmentTypeName}</p>
            </div>
            <span class="badge ${badgeClass}">${badgeText}</span>
        </div>
        <div class="info">
            <p><strong>Fecha:</strong> ${formattedDate}</p>
            <p><strong>Hora:</strong> ${formattedTime}</p>
            ${appointment.details ? `<p class="details"><strong>Detalles:</strong> ${appointment.details}</p>` : ''}
        </div>
        ${appointment.status === 'scheduled' && appointment.teamsLink ? `
            <div class="teams-join-section">
                <a href="${appointment.teamsLink}" target="_blank" rel="noopener noreferrer" class="btn-join-meeting">
                    <i class="fas fa-video"></i> Unirse a la Cita
                </a>
            </div>
        ` : ''}
        <div class="actions">
            ${appointment.status === 'scheduled' ? `
                <button class="btn-cancel-appointment" data-id="${appointment.id}">
                    Cancelar cita
                </button>
            ` : ''}
        </div>
    `;

    // Agregar evento para cancelar cita
    if (appointment.status === 'scheduled') {
        const cancelButton = appointmentEl.querySelector('.btn-cancel-appointment');
        cancelButton.addEventListener('click', () => {
            selectedAppointmentId = appointment.id;
            cancelModal.classList.add('active');
        });
    }

    return appointmentEl;
}

/**
 * Manejar confirmación de cancelación de cita
 */
async function handleCancelConfirmation() {
    if (!selectedAppointmentId) return;

    try {
        // Deshabilitar botón mientras se procesa
        btnConfirmCancel.disabled = true;
        btnConfirmCancel.textContent = 'Procesando...';

        // Enviar solicitud al servidor
        await API.updateAppointmentStatus(selectedAppointmentId, 'cancelled');

        // Cerrar modal
        cancelModal.classList.remove('active');

        // Mostrar notificación
        showNotification('La cita ha sido cancelada exitosamente.');

        // Recargar citas
        loadUserAppointments();

        // Resetear
        selectedAppointmentId = null;
        btnConfirmCancel.disabled = false;
        btnConfirmCancel.textContent = 'Cancelar cita';

        // Si está en vista administrador, actualizar el calendario y próximas citas
        if (!adminView.classList.contains('hidden')) {
            renderCalendar();
            loadUpcomingAppointments();
        }
    } catch (error) {
        console.error('Error al cancelar la cita:', error);
        showNotification('Error al cancelar la cita. Por favor, intente nuevamente.', 'error');

        // Resetear
        btnConfirmCancel.disabled = false;
        btnConfirmCancel.textContent = 'Cancelar cita';
    }
}

/**
 * Mostrar notificación
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de notificación (success o error)
 */
function showNotification(message, type = 'success') {
    notificationMessage.textContent = message;

    // Cambiar color según tipo
    if (type === 'error') {
        successNotification.querySelector('.notification-content').style.backgroundColor = 'var(--danger)';
    } else {
        successNotification.querySelector('.notification-content').style.backgroundColor = 'var(--success)';
    }

    // Mostrar notificación
    successNotification.classList.add('active');

    // Ocultar después de 3 segundos
    setTimeout(() => {
        successNotification.classList.remove('active');
    }, 3000);
}

/**
 * Obtener nombre del tipo de cita por ID
 * @param {number} typeId - ID del tipo de cita
 * @returns {string} - Nombre del tipo de cita
 */
function getAppointmentTypeName(typeId) {
    const type = appointmentTypes.find(t => t.id === typeId);
    return type ? type.name : 'Tipo desconocido';
}

/**
 * Obtener nombre del programa por ID
 * @param {number} programId - ID del programa académico
 * @returns {string} - Nombre del programa
 */
function getProgramName(programId) {
    const program = programs.find(p => p.id === programId);
    return program ? program.name : 'Programa desconocido';
}

/**
 * Manejar búsqueda por documento
 */
async function handleDocumentSearch() {
    const documentNumber = searchDocument.value.trim();

    if (!documentNumber) {
        showNotification('Por favor, ingrese un número de documento válido.', 'error');
        return;
    }

    try {
        const appointments = await API.getAppointmentsByDocument(documentNumber);

        // Limpiar contenedor de citas
        appointmentsContainer.innerHTML = '';

        if (appointments.length === 0) {
            appointmentsContainer.innerHTML = '<div class="empty-appointments">No se encontraron citas para este documento.</div>';
            return;
        }

        // Ordenar citas por fecha
        appointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Renderizar cada cita
        appointments.forEach(appointment => {
            const appointmentEl = createAppointmentElement(appointment);
            appointmentsContainer.appendChild(appointmentEl);
        });

        // Mostrar notificación
        showNotification(`Se encontraron ${appointments.length} citas para el documento ${documentNumber}.`);
    } catch (error) {
        console.error('Error al buscar citas:', error);
        appointmentsContainer.innerHTML = '<div class="empty-appointments">Error al buscar citas. Por favor, intente nuevamente.</div>';
    }
}

/**
 * Renderizar calendario según la vista seleccionada
 */
async function renderCalendar() {
    try {
        // Obtener todas las citas
        const appointments = await API.getAppointments();

        // Actualizar título según la vista
        updateCalendarTitle();

        // Limpiar contenido del calendario
        calendarBody.innerHTML = '';

        // Renderizar según la vista seleccionada
        if (calendarView === 'month') {
            createMonthCalendar(appointments);
        } else if (calendarView === 'week') {
            createWeekCalendar(appointments);
        } else if (calendarView === 'day') {
            createDayCalendar(appointments);
        }
    } catch (error) {
        console.error('Error al renderizar calendario:', error);
        calendarBody.innerHTML = '<div class="error-message">Error al cargar el calendario. Por favor, intente nuevamente.</div>';
    }
}

/**
 * Actualizar título del calendario según la vista seleccionada
 */
function updateCalendarTitle() {
    const monthNames = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    const dayNames = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];

    if (calendarView === 'month') {
        currentMonthYear.textContent = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
    } else if (calendarView === 'week') {
        // Obtener el primer día de la semana (domingo)
        const firstDayOfWeek = new Date(currentDate);
        firstDayOfWeek.setDate(currentDate.getDate() - currentDate.getDay());

        // Obtener el último día de la semana (sábado)
        const lastDayOfWeek = new Date(firstDayOfWeek);
        lastDayOfWeek.setDate(firstDayOfWeek.getDate() + 6);

        // Formatear fechas
        const startStr = `${firstDayOfWeek.getDate()} ${monthNames[firstDayOfWeek.getMonth()]}`;
        const endStr = `${lastDayOfWeek.getDate()} ${monthNames[lastDayOfWeek.getMonth()]} ${lastDayOfWeek.getFullYear()}`;

        currentMonthYear.textContent = `${startStr} - ${endStr}`;
    } else if (calendarView === 'day') {
        const formattedDate = `${dayNames[currentDate.getDay()]}, ${currentDate.getDate()} de ${monthNames[currentDate.getMonth()]} de ${currentDate.getFullYear()}`;
        currentMonthYear.textContent = formattedDate;
    }
}

/**
 * Crear vista del calendario mensual
 * @param {Array} appointments - Lista de citas
 */
function createMonthCalendar(appointments) {
    // Obtener primer día del mes actual
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    // Obtener último día del mes actual
    const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

    // Obtener día de la semana del primer día (0: domingo, 6: sábado)
    const firstDayOfWeek = firstDay.getDay();

    // Calcular cuántas celdas del mes anterior necesitamos
    const prevMonthCells = firstDayOfWeek;

    // Calcular cuántas celdas del mes siguiente necesitamos
    // Para una vista de 6 semanas (42 celdas)
    const totalCells = 42;
    const currentMonthDays = lastDay.getDate();
    const remainingCells = totalCells - prevMonthCells - currentMonthDays;

    // Crear celdas para los días del mes anterior
    for (let i = 0; i < prevMonthCells; i++) {
        const prevMonthDay = new Date(firstDay);
        prevMonthDay.setDate(prevMonthDay.getDate() - (prevMonthCells - i));

        const dayCell = createDayCell(prevMonthDay, appointments, true);
        calendarBody.appendChild(dayCell);
    }

    // Crear celdas para los días del mes actual
    for (let i = 1; i <= currentMonthDays; i++) {
        const currentMonthDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), i);

        const dayCell = createDayCell(currentMonthDay, appointments, false);
        calendarBody.appendChild(dayCell);
    }

    // Crear celdas para los días del mes siguiente
    for (let i = 1; i <= remainingCells; i++) {
        const nextMonthDay = new Date(lastDay);
        nextMonthDay.setDate(nextMonthDay.getDate() + i);

        const dayCell = createDayCell(nextMonthDay, appointments, true);
        calendarBody.appendChild(dayCell);
    }
}

/**
 * Crear celda para un día del calendario
 * @param {Date} date - Fecha
 * @param {Array} appointments - Lista de citas
 * @param {boolean} isOtherMonth - Indicador si es de otro mes
 * @returns {HTMLElement} - Elemento de celda del día
 */
function createDayCell(date, appointments, isOtherMonth = false) {
    const dayCell = document.createElement('div');
    dayCell.className = 'calendar-day';

    // Añadir clase si es de otro mes
    if (isOtherMonth) {
        dayCell.classList.add('other-month');
    }

    // Añadir clase si es hoy
    const today = new Date();
    if (date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()) {
        dayCell.classList.add('today');
    }

    // Añadir número del día
    const dayNumber = document.createElement('div');
    dayNumber.className = 'day-number';
    dayNumber.textContent = date.getDate();
    dayCell.appendChild(dayNumber);

    // Filtrar citas para este día
    const dayAppointments = appointments.filter(appointment => {
        const appointmentDate = new Date(appointment.date);
        return (
            appointmentDate.getDate() === date.getDate() &&
            appointmentDate.getMonth() === date.getMonth() &&
            appointmentDate.getFullYear() === date.getFullYear()
        );
    });

    // Añadir citas a la celda
    if (dayAppointments.length > 0) {
        // Ordenar por hora
        dayAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Limitar a 3 citas visibles para no sobrecargar la celda
        const visibleAppointments = dayAppointments.slice(0, 3);
        const hasMoreAppointments = dayAppointments.length > 3;

        // Crear elementos para cada cita
        visibleAppointments.forEach(appointment => {
            const appointmentTime = new Date(appointment.date).toLocaleTimeString('es-ES', {
                hour: '2-digit',
                minute: '2-digit'
            });

            // Clase según estado
            let statusClass = 'scheduled';
            if (appointment.status === 'completed') {
                statusClass = 'completed';
            } else if (appointment.status === 'cancelled') {
                statusClass = 'cancelled';
            }

            // Crear elemento de la cita
            const appointmentEl = document.createElement('div');
            appointmentEl.className = `calendar-event ${statusClass}`;
            appointmentEl.textContent = `${appointmentTime} - ${appointment.userName}`;
            appointmentEl.title = `${appointment.appointmentName} - ${appointment.userName}`;

            // Agregar evento para mostrar detalles
            appointmentEl.addEventListener('click', () => showAppointmentDetails(appointment));

            dayCell.appendChild(appointmentEl);
        });

        // Indicador de más citas
        if (hasMoreAppointments) {
            const moreIndicator = document.createElement('div');
            moreIndicator.className = 'more-indicator';
            moreIndicator.textContent = `+ ${dayAppointments.length - 3} más`;
            dayCell.appendChild(moreIndicator);
        }
    }

    return dayCell;
}

/**
 * Crear vista del calendario semanal
 * @param {Array} appointments - Lista de citas
 */
function createWeekCalendar(appointments) {
    // Obtener el primer día de la semana (domingo)
    const firstDayOfWeek = new Date(currentDate);
    firstDayOfWeek.setDate(currentDate.getDate() - currentDate.getDay());

    // Crear contenedor de vista semanal
    const weekView = document.createElement('div');
    weekView.className = 'calendar-week-view';

    // Crear fila de encabezado con días
    const headerRow = document.createElement('div');
    headerRow.className = 'week-header-row';

    // Celda vacía para la columna de horas
    const emptyHeaderCell = document.createElement('div');
    emptyHeaderCell.className = 'week-header-cell';
    headerRow.appendChild(emptyHeaderCell);

    // Crear celdas de encabezado para cada día
    const dayNames = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];

    for (let i = 0; i < 7; i++) {
        const day = new Date(firstDayOfWeek);
        day.setDate(firstDayOfWeek.getDate() + i);

        const headerCell = document.createElement('div');
        headerCell.className = 'week-header-cell';

        // Agregar clase si es hoy
        const today = new Date();
        if (day.getDate() === today.getDate() &&
            day.getMonth() === today.getMonth() &&
            day.getFullYear() === today.getFullYear()) {
            headerCell.classList.add('today');
        }

        const dayName = document.createElement('div');
        dayName.className = 'day-name';
        dayName.textContent = dayNames[i];

        const dayNumber = document.createElement('div');
        dayNumber.className = 'day-number';
        dayNumber.textContent = day.getDate();

        headerCell.appendChild(dayName);
        headerCell.appendChild(dayNumber);
        headerRow.appendChild(headerCell);
    }

    weekView.appendChild(headerRow);

    // Crear filas para cada hora (7:00 - 20:00)
    const startHour = 7;
    const endHour = 20;

    for (let hour = startHour; hour <= endHour; hour++) {
        const timeRow = document.createElement('div');
        timeRow.className = 'week-time-row';

        // Celda de hora
        const timeCell = document.createElement('div');
        timeCell.className = 'week-time-cell';
        timeCell.textContent = `${hour}:00`;
        timeRow.appendChild(timeCell);

        // Celdas para cada día de la semana
        for (let day = 0; day < 7; day++) {
            const date = new Date(firstDayOfWeek);
            date.setDate(firstDayOfWeek.getDate() + day);
            date.setHours(hour, 0, 0, 0);

            const dayHourCell = document.createElement('div');
            dayHourCell.className = 'week-day-hour-cell';

            // Agregar clase si es hoy
            const today = new Date();
            if (date.getDate() === today.getDate() &&
                date.getMonth() === today.getMonth() &&
                date.getFullYear() === today.getFullYear()) {
                dayHourCell.classList.add('today');
            }

            // Filtrar citas para esta hora y día
            const hourAppointments = appointments.filter(appointment => {
                const appointmentDate = new Date(appointment.date);
                return (
                    appointmentDate.getDate() === date.getDate() &&
                    appointmentDate.getMonth() === date.getMonth() &&
                    appointmentDate.getFullYear() === date.getFullYear() &&
                    appointmentDate.getHours() === hour
                );
            });

            // Agregar citas a la celda
            if (hourAppointments.length > 0) {
                hourAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

                hourAppointments.forEach(appointment => {
                    const appointmentEl = createWeekAppointmentElement(appointment);
                    dayHourCell.appendChild(appointmentEl);
                });
            }

            timeRow.appendChild(dayHourCell);
        }

        weekView.appendChild(timeRow);
    }

    calendarBody.appendChild(weekView);
}

/**
 * Crear elemento para una cita en la vista semanal
 * @param {Object} appointment - Datos de la cita
 * @returns {HTMLElement} - Elemento de la cita
 */
function createWeekAppointmentElement(appointment) {
    const appointmentEl = document.createElement('div');

    // Clase según estado
    let statusClass = 'scheduled';
    if (appointment.status === 'completed') {
        statusClass = 'completed';
    } else if (appointment.status === 'cancelled') {
        statusClass = 'cancelled';
    }

    appointmentEl.className = `week-appointment ${statusClass}`;

    const appointmentDate = new Date(appointment.date);
    const appointmentTime = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    appointmentEl.innerHTML = `
        <div class="appointment-time">${appointmentTime}</div>
        <div class="appointment-title">${appointment.appointmentName}</div>
        <div class="appointment-user">${appointment.userName}</div>
    `;

    // Agregar evento para mostrar detalles
    appointmentEl.addEventListener('click', () => showAppointmentDetails(appointment));

    return appointmentEl;
}

/**
 * Crear vista del calendario diario
 * @param {Array} appointments - Lista de citas
 */
function createDayCalendar(appointments) {
    // Crear contenedor de vista diaria
    const dayView = document.createElement('div');
    dayView.className = 'calendar-day-view';

    // Crear filas para cada hora (7:00 - 20:00)
    const startHour = 7;
    const endHour = 20;

    for (let hour = startHour; hour <= endHour; hour++) {
        const timeRow = document.createElement('div');
        timeRow.className = 'day-time-row';

        // Celda de hora
        const timeCell = document.createElement('div');
        timeCell.className = 'day-time-cell';
        timeCell.textContent = `${hour}:00`;
        timeRow.appendChild(timeCell);

        // Celda de citas para esta hora
        const appointmentsCell = document.createElement('div');
        appointmentsCell.className = 'day-appointments-cell';

        // Filtrar citas para esta hora
        const hourAppointments = appointments.filter(appointment => {
            const appointmentDate = new Date(appointment.date);
            return (
                appointmentDate.getDate() === currentDate.getDate() &&
                appointmentDate.getMonth() === currentDate.getMonth() &&
                appointmentDate.getFullYear() === currentDate.getFullYear() &&
                appointmentDate.getHours() === hour
            );
        });

        // Agregar citas a la celda
        if (hourAppointments.length > 0) {
            hourAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

            hourAppointments.forEach(appointment => {
                const appointmentEl = createDayAppointmentElement(appointment);
                appointmentsCell.appendChild(appointmentEl);
            });
        } else {
            // Mostrar mensaje de que no hay citas
            const emptySlot = document.createElement('div');
            emptySlot.className = 'day-empty-slot';
            emptySlot.textContent = 'No hay citas programadas';
            appointmentsCell.appendChild(emptySlot);
        }

        timeRow.appendChild(appointmentsCell);
        dayView.appendChild(timeRow);
    }

    calendarBody.appendChild(dayView);
}

/**
 * Crear elemento para una cita en la vista diaria
 * @param {Object} appointment - Datos de la cita
 * @returns {HTMLElement} - Elemento de la cita
 */
function createDayAppointmentElement(appointment) {
    const appointmentEl = document.createElement('div');

    // Clase según estado
    let statusClass = 'scheduled';
    let statusText = 'Programada';

    if (appointment.status === 'completed') {
        statusClass = 'completed';
        statusText = 'Completada';
    } else if (appointment.status === 'cancelled') {
        statusClass = 'cancelled';
        statusText = 'Cancelada';
    }

    appointmentEl.className = `day-appointment ${statusClass}`;

    const appointmentDate = new Date(appointment.date);
    const appointmentTime = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // Obtener tipo de cita
    const appointmentTypeName = getAppointmentTypeName(appointment.typeOfAppointment);

    appointmentEl.innerHTML = `
        <div class="appointment-header">
            <span class="appointment-time">${appointmentTime}</span>
            <span class="appointment-status">${statusText}</span>
        </div>
        <div class="appointment-title">${appointment.appointmentName}</div>
        <div class="appointment-details">
            <div><strong>Estudiante:</strong> ${appointment.userName}</div>
            <div><strong>Tipo:</strong> ${appointmentTypeName}</div>
            ${appointment.details ? `<div><strong>Detalles:</strong> ${appointment.details}</div>` : ''}
        </div>
    `;

    // Agregar evento para mostrar detalles
    appointmentEl.addEventListener('click', () => showAppointmentDetails(appointment));

    return appointmentEl;
}

/**
 * Cambiar vista del calendario
 * @param {string} view - Nombre de la vista (month, week o day)
 */
function setCalendarView(view) {
    // Actualizar variable global
    calendarView = view;

    // Actualizar botones
    btnMonthView.classList.remove('active');
    btnWeekView.classList.remove('active');
    btnDayView.classList.remove('active');

    if (view === 'month') {
        btnMonthView.classList.add('active');
    } else if (view === 'week') {
        btnWeekView.classList.add('active');
    } else if (view === 'day') {
        btnDayView.classList.add('active');
    }

    // Renderizar calendario
    renderCalendar();
}

/**
 * Cargar próximas citas
 */
async function loadUpcomingAppointments() {
    try {
        const allAppointments = await API.getAppointments();

        // Limpiar contenedor
        upcomingAppointments.innerHTML = '';

        // Filtrar citas futuras y no canceladas
        const now = new Date();
        const futureAppointments = allAppointments.filter(appointment => {
            const appointmentDate = new Date(appointment.date);
            return appointmentDate >= now && appointment.status !== 'cancelled';
        });

        // Ordenar por fecha (más cercana primero)
        futureAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Mostrar máximo 5 citas
        const visibleAppointments = futureAppointments.slice(0, 5);

        if (visibleAppointments.length === 0) {
            upcomingAppointments.innerHTML = '<div class="empty-appointments">No hay citas próximas.</div>';
            return;
        }

        // Renderizar cada cita
        visibleAppointments.forEach(appointment => {
            const appointmentEl = createUpcomingAppointmentElement(appointment);
            upcomingAppointments.appendChild(appointmentEl);
        });
    } catch (error) {
        console.error('Error al cargar próximas citas:', error);
        upcomingAppointments.innerHTML = '<div class="empty-appointments">Error al cargar próximas citas. Por favor, intente nuevamente.</div>';
    }
}

/**
 * Crear elemento para una cita próxima
 * @param {Object} appointment - Datos de la cita
 * @returns {HTMLElement} - Elemento de la cita
 */
function createUpcomingAppointmentElement(appointment) {
    const appointmentEl = document.createElement('div');

    // Clase según estado
    let statusClass = '';
    let statusText = 'Programada';

    if (appointment.status === 'completed') {
        statusClass = 'completed';
        statusText = 'Completada';
    }

    appointmentEl.className = `upcoming-appointment ${statusClass}`;

    const appointmentDate = new Date(appointment.date);
    const formattedDate = appointmentDate.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'short'
    });

    const formattedTime = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // Obtener tipo de cita
    const appointmentTypeName = getAppointmentTypeName(appointment.typeOfAppointment);

    appointmentEl.innerHTML = `
        <div class="meta">
            <span class="type-badge">${appointmentTypeName}</span>
            <span class="status-badge badge ${appointment.status === 'completed' ? 'badge-completed' : 'badge-scheduled'}">${statusText}</span>
        </div>
        <h3 class="title">${appointment.appointmentName}</h3>
        <div class="date-time">
            <i class="fas fa-calendar-alt"></i> ${formattedDate} - ${formattedTime}
        </div>
        <div class="info">
            <strong>Estudiante:</strong> ${appointment.userName}
        </div>
    `;

    // Agregar evento para mostrar detalles
    appointmentEl.addEventListener('click', () => showAppointmentDetails(appointment));

    return appointmentEl;
}

/**
 * Mostrar detalles de una cita
 * @param {Object} appointment - Datos de la cita
 */
function showAppointmentDetails(appointment) {
    selectedAppointment = appointment;

    // Obtener nombres
    const appointmentTypeName = getAppointmentTypeName(appointment.typeOfAppointment);
    const programName = getProgramName(appointment.programId);

    // Formato de fecha y hora
    const appointmentDate = new Date(appointment.date);
    const formattedDate = appointmentDate.toLocaleDateString('es-ES', {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });

    const formattedTime = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // Actualizar contenido del modal
    appointmentDetailsContent.innerHTML = `
        <div class="appointment-details-header">
            <h3>${appointment.appointmentName}</h3>
            <div class="badge ${appointment.status === 'scheduled' ? 'badge-scheduled' : appointment.status === 'completed' ? 'badge-completed' : 'badge-cancelled'}">
                ${appointment.status === 'scheduled' ? 'Programada' : appointment.status === 'completed' ? 'Completada' : 'Cancelada'}
            </div>
        </div>

        <div class="appointment-details-info">
            <div class="appointment-details-row">
                <div class="appointment-detail">
                    <div class="label">Fecha</div>
                    <div class="value">${formattedDate}</div>
                </div>
                <div class="appointment-detail">
                    <div class="label">Hora</div>
                    <div class="value">${formattedTime}</div>
                </div>
            </div>

            <div class="appointment-details-row">
                <div class="appointment-detail">
                    <div class="label">Estudiante</div>
                    <div class="value">${appointment.userName}</div>
                </div>
                <div class="appointment-detail">
                    <div class="label">Documento</div>
                    <div class="value">${appointment.numberDocument}</div>
                </div>
            </div>

            <div class="appointment-details-row">
                <div class="appointment-detail">
                    <div class="label">Correo</div>
                    <div class="value">${appointment.userEmail}</div>
                </div>
                <div class="appointment-detail">
                    <div class="label">Teléfono</div>
                    <div class="value">${appointment.phone}</div>
                </div>
            </div>

            <div class="appointment-details-row">
                <div class="appointment-detail">
                    <div class="label">Programa</div>
                    <div class="value">${programName}</div>
                </div>
                <div class="appointment-detail">
                    <div class="label">Tipo de cita</div>
                    <div class="value">${appointmentTypeName}</div>
                </div>
            </div>

            ${appointment.details ? `
                <div class="appointment-description">
                    <div class="label">Detalles</div>
                    <div class="value">${appointment.details}</div>
                </div>
            ` : ''}

            ${appointment.status === 'scheduled' && appointment.teamsLink ? `
                <div class="teams-join-section">
                    <a href="${appointment.teamsLink}" target="_blank" rel="noopener noreferrer" class="btn-join-meeting">
                        <i class="fas fa-video"></i> Unirse a la Cita
                    </a>
                </div>
            ` : ''}
        </div>
    `;

    // Actualizar campo de estado
    statusSelect.value = appointment.status;

    // Actualizar vista del enlace de Teams
    updateTeamsLinkDisplay(appointment.teamsLink);

    // Mostrar modal
    appointmentDetailsModal.classList.add('active');
}

/**
 * Cerrar modal de detalles de cita
 */
function closeAppointmentDetails() {
    appointmentDetailsModal.classList.remove('active');
    selectedAppointment = null;
    hideTeamsLinkEdit();
}

/**
 * Actualizar vista del enlace de Teams
 * @param {string|null} teamsLink - Enlace de Teams
 */
function updateTeamsLinkDisplay(teamsLink) {
    if (teamsLink) {
        teamsLinkDisplay.innerHTML = `
            <a href="${teamsLink}" target="_blank" class="teams-link">
                <i class="fas fa-video"></i> ${teamsLink.length > 50 ? teamsLink.substring(0, 50) + '...' : teamsLink}
                <span class="btn-sm ml-2">Abrir enlace</span>
            </a>
        `;
    } else {
        teamsLinkDisplay.innerHTML = `
            <p class="no-link-message">No se ha establecido un enlace de Teams para esta cita.</p>
        `;
    }

    // Actualizar input
    teamsLinkInput.value = teamsLink || '';
}

/**
 * Mostrar editor de enlace de Teams
 */
function showTeamsLinkEdit() {
    teamsLinkDisplay.classList.add('hidden');
    teamsLinkEdit.classList.remove('hidden');
    btnEditTeamsLink.classList.add('hidden');
}

/**
 * Ocultar editor de enlace de Teams
 */
function hideTeamsLinkEdit() {
    teamsLinkDisplay.classList.remove('hidden');
    teamsLinkEdit.classList.add('hidden');
    btnEditTeamsLink.classList.remove('hidden');
}

/**
 * Guardar enlace de Teams
 */
async function saveTeamsLink() {
    if (!selectedAppointment) return;

    const teamsLink = teamsLinkInput.value.trim();

    // Validar URL
    if (teamsLink && !isValidUrl(teamsLink)) {
        showNotification('Por favor, ingrese una URL válida.', 'error');
        return;
    }

    try {
        // Deshabilitar botón mientras se procesa
        btnSaveTeamsLink.disabled = true;
        btnSaveTeamsLink.textContent = 'Guardando...';

        // Enviar solicitud al servidor
        await API.updateAppointmentTeamsLink(selectedAppointment.id, teamsLink || null);

        // Actualizar cita seleccionada
        selectedAppointment.teamsLink = teamsLink || null;

        // Actualizar vista
        updateTeamsLinkDisplay(teamsLink || null);

        // Ocultar editor
        hideTeamsLinkEdit();

        // Mostrar notificación
        showNotification('Enlace de Teams actualizado correctamente.');

        // Recargar calendario y próximas citas
        renderCalendar();
        loadUpcomingAppointments();
    } catch (error) {
        console.error('Error al guardar enlace de Teams:', error);
        showNotification('Error al guardar enlace de Teams. Por favor, intente nuevamente.', 'error');
    } finally {
        // Resetear botón
        btnSaveTeamsLink.disabled = false;
        btnSaveTeamsLink.textContent = 'Guardar';
    }
}

/**
 * Validar si una cadena es una URL válida
 * @param {string} url - URL a validar
 * @returns {boolean} - true si es válida, false si no
 */
function isValidUrl(url) {
    try {
        new URL(url);
        return true;
    } catch (error) {
        return false;
    }
}

/**
 * Manejar actualización de estado de cita
 */
async function handleStatusUpdate() {
    if (!selectedAppointment) return;

    const newStatus = statusSelect.value;

    // Si el estado no ha cambiado, no hacer nada
    if (newStatus === selectedAppointment.status) {
        return;
    }

    try {
        // Deshabilitar botón mientras se procesa
        btnUpdateStatus.disabled = true;
        btnUpdateStatus.textContent = 'Actualizando...';

        // Enviar solicitud al servidor
        await API.updateAppointmentStatus(selectedAppointment.id, newStatus);

        // Actualizar cita seleccionada
        selectedAppointment.status = newStatus;

        // Cerrar modal
        closeAppointmentDetails();

        // Mostrar notificación
        showNotification('Estado de la cita actualizado correctamente.');

        // Recargar calendario y próximas citas
        renderCalendar();
        loadUpcomingAppointments();
    } catch (error) {
        console.error('Error al actualizar estado de la cita:', error);
        showNotification('Error al actualizar estado de la cita. Por favor, intente nuevamente.', 'error');
    } finally {
        // Resetear botón
        btnUpdateStatus.disabled = false;
        btnUpdateStatus.textContent = 'Actualizar estado';
    }
}