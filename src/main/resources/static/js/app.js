// Referencias a elementos del DOM
const studentView = document.getElementById('student-view');
const adminView = document.getElementById('admin-view');
const documentSearchView = document.getElementById('document-search-view');
const btnStudentView = document.getElementById('btn-student-view');
const btnAdminView = document.getElementById('btn-admin-view');
const btnDocumentSearch = document.getElementById('btn-document-search');
const appointmentForm = document.getElementById('appointment-form');
const appointmentsContainer = document.getElementById('appointments-container');
const cancelModal = document.getElementById('cancel-modal');
const btnCancelBack = document.getElementById('btn-cancel-back');
const btnConfirmCancel = document.getElementById('btn-confirm-cancel');
const successNotification = document.getElementById('success-notification');
const notificationMessage = document.getElementById('notification-message');
const btnCloseNotification = document.getElementById('btn-close-notification');
const documentSearchForm = document.getElementById('document-search-form');
const searchResults = document.getElementById('search-results');
const upcomingAppointments = document.getElementById('upcoming-appointments');
const calendarBody = document.getElementById('calendar-body');
const currentMonthYear = document.getElementById('current-month-year');
const btnPrevMonth = document.getElementById('btn-prev-month');
const btnNextMonth = document.getElementById('btn-next-month');
const btnToday = document.getElementById('btn-today');
const btnMonthView = document.getElementById('btn-month-view');
const btnWeekView = document.getElementById('btn-week-view');
const btnDayView = document.getElementById('btn-day-view');

// Variables globales
let selectedAppointmentId = null;
let currentDate = new Date();
let userEmail = 'maria.gonzalez@example.com'; // Email de ejemplo para el usuario actual
let appointmentTypes = [];
let calendarView = 'month';

// Inicialización
document.addEventListener('DOMContentLoaded', init);

async function init() {
    // Cargar datos iniciales
    await loadFormData();

    // Configurar manejadores de eventos
    setupEventListeners();

    // Mostrar vista por defecto (estudiante)
    switchView('student');

    // Cargar citas del usuario actual
    await loadUserAppointments();
}

// Cargar datos para los formularios (programas y tipos de citas)
async function loadFormData() {
    // Cargar programas académicos
    const programs = await API.getPrograms();
    const programSelect = document.getElementById('programId');

    programs.forEach(program => {
        const option = document.createElement('option');
        option.value = program.id;
        option.textContent = program.name;
        programSelect.appendChild(option);
    });

    // Cargar tipos de citas
    appointmentTypes = await API.getAppointmentTypes();
    const typeSelect = document.getElementById('typeOfAppointment');

    appointmentTypes.forEach(type => {
        const option = document.createElement('option');
        option.value = type.id;
        option.textContent = type.name;
        typeSelect.appendChild(option);
    });
}

// Configurar listeners de eventos
function setupEventListeners() {
    // Cambio de vistas
    btnStudentView.addEventListener('click', () => switchView('student'));
    btnAdminView.addEventListener('click', () => switchView('admin'));
    btnDocumentSearch.addEventListener('click', () => switchView('document-search'));

    // Formulario de creación de citas
    appointmentForm.addEventListener('submit', handleAppointmentSubmit);

    // Modal de cancelación
    btnCancelBack.addEventListener('click', () => {
        cancelModal.classList.remove('active');
    });

    btnConfirmCancel.addEventListener('click', handleCancelConfirmation);

    // Notificación
    btnCloseNotification.addEventListener('click', () => {
        successNotification.classList.remove('active');
    });

    // Búsqueda por documento
    documentSearchForm.addEventListener('submit', handleDocumentSearch);

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
    btnMonthView.addEventListener('click', () => {
        setCalendarView('month');
    });

    btnWeekView.addEventListener('click', () => {
        setCalendarView('week');
    });

    btnDayView.addEventListener('click', () => {
        setCalendarView('day');
    });
}

// Cambiar entre las vistas de la aplicación
function switchView(view) {
    // Ocultar todas las vistas
    studentView.classList.add('hidden');
    adminView.classList.add('hidden');
    documentSearchView.classList.add('hidden');

    // Desactivar todos los botones de navegación
    btnStudentView.classList.remove('active');
    btnAdminView.classList.remove('active');
    btnDocumentSearch.classList.remove('active');

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
    } else if (view === 'document-search') {
        documentSearchView.classList.remove('hidden');
        btnDocumentSearch.classList.add('active');
    }
}

// Manejar el envío del formulario de citas
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
        details: formData.get('details'),
        status: 'scheduled'
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

// Cargar citas del usuario actual
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

// Crear elemento HTML para una cita
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
        ${appointment.status === 'scheduled' ? `
            <div class="actions">
                <button class="btn-cancel-appointment" data-id="${appointment.id}">
                    Cancelar cita
                </button>
            </div>
        ` : ''}
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

// Manejar confirmación de cancelación de cita
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

// Mostrar notificación
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

// Obtener nombre del tipo de cita por ID
function getAppointmentTypeName(typeId) {
    const type = appointmentTypes.find(t => t.id === typeId);
    return type ? type.name : 'Tipo desconocido';
}

// Manejar búsqueda por documento
async function handleDocumentSearch(event) {
    event.preventDefault();

    const documentNumber = document.getElementById('searchDocument').value.trim();

    if (!documentNumber) {
        showNotification('Por favor, ingrese un número de documento válido.', 'error');
        return;
    }

    try {
        const appointments = await API.getAppointmentsByDocument(documentNumber);

        // Limpiar resultados previos
        searchResults.innerHTML = '';

        if (appointments.length === 0) {
            searchResults.innerHTML = '<div class="empty-appointments">No se encontraron citas para este documento.</div>';
            return;
        }

        // Crear contenedor para resultados
        const resultsContainer = document.createElement('div');
        resultsContainer.className = 'appointments-container';

        // Ordenar citas por fecha
        appointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Añadir cada cita al contenedor
        appointments.forEach(appointment => {
            const appointmentEl = createAppointmentElement(appointment);
            resultsContainer.appendChild(appointmentEl);
        });

        searchResults.appendChild(resultsContainer);
    } catch (error) {
        console.error('Error al buscar citas:', error);
        searchResults.innerHTML = '<div class="empty-appointments">Error al buscar citas. Por favor, intente nuevamente.</div>';
    }
}

// Renderizar calendario
async function renderCalendar() {
    try {
        // Obtener todas las citas
        const appointments = await API.getAppointments();

        // Actualizar título según la vista
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

        // Limpiar calendario
        calendarBody.innerHTML = '';

        // Renderizar la vista adecuada
        if (calendarView === 'month') {
            createCalendarCells(appointments);
        } else if (calendarView === 'week') {
            createWeekView(appointments);
        } else if (calendarView === 'day') {
            createDayView(appointments);
        }
    } catch (error) {
        console.error('Error al cargar el calendario:', error);
        calendarBody.innerHTML = '<div class="error-message">Error al cargar el calendario. Por favor, intente nuevamente.</div>';
    }
}

// Crear celdas del calendario
function createCalendarCells(appointments) {
    // Obtener el primer día del mes
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);

    // Obtener el último día del mes
    const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

    // Obtener el día de la semana del primer día (0-6, donde 0 es Domingo)
    const firstDayOfWeek = firstDay.getDay();

    // Crear celdas de los días previos al mes actual
    for (let i = 0; i < firstDayOfWeek; i++) {
        const prevMonthDay = new Date(firstDay);
        prevMonthDay.setDate(prevMonthDay.getDate() - (firstDayOfWeek - i));

        const dayCell = createDayCell(prevMonthDay, appointments, true);
        calendarBody.appendChild(dayCell);
    }

    // Crear celdas para los días del mes actual
    for (let day = 1; day <= lastDay.getDate(); day++) {
        const currentMonthDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);

        const dayCell = createDayCell(currentMonthDay, appointments);
        calendarBody.appendChild(dayCell);
    }

    // Calcular número de celdas a completar
    const totalCells = 42; // 6 filas x 7 días
    const remainingCells = totalCells - (firstDayOfWeek + lastDay.getDate());

    // Crear celdas para los días siguientes al mes actual
    for (let i = 1; i <= remainingCells; i++) {
        const nextMonthDay = new Date(lastDay);
        nextMonthDay.setDate(nextMonthDay.getDate() + i);

        const dayCell = createDayCell(nextMonthDay, appointments, true);
        calendarBody.appendChild(dayCell);
    }
}

// Crear celda para un día del calendario
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

// Cargar próximas citas
async function loadUpcomingAppointments() {
    try {
        const allAppointments = await API.getAppointments();

        // Filtrar solo citas programadas
        const scheduledAppointments = allAppointments.filter(
            appointment => appointment.status === 'scheduled'
        );

        // Ordenar por fecha (ascendente)
        scheduledAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

        // Tomar las 5 primeras
        const upcomingItems = scheduledAppointments.slice(0, 5);

        // Limpiar contenedor
        upcomingAppointments.innerHTML = '';

        if (upcomingItems.length === 0) {
            upcomingAppointments.innerHTML = '<div class="empty-appointments">No hay citas próximas.</div>';
            return;
        }

        // Crear elementos para cada cita
        upcomingItems.forEach(appointment => {
            const appointmentEl = createUpcomingAppointmentElement(appointment);
            upcomingAppointments.appendChild(appointmentEl);
        });
    } catch (error) {
        console.error('Error al cargar próximas citas:', error);
        upcomingAppointments.innerHTML = '<div class="empty-appointments">Error al cargar las citas. Por favor, intente nuevamente.</div>';
    }
}

// Crear elemento HTML para una cita próxima
function createUpcomingAppointmentElement(appointment) {
    const appointmentEl = document.createElement('div');
    appointmentEl.className = 'upcoming-appointment';

    // Formatear fecha
    const appointmentDate = new Date(appointment.date);

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    let dateLabel = appointmentDate.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'short'
    });

    // Si es hoy, mostrar "Hoy" en lugar de la fecha
    if (appointmentDate.getDate() === today.getDate() &&
        appointmentDate.getMonth() === today.getMonth() &&
        appointmentDate.getFullYear() === today.getFullYear()) {
        dateLabel = 'Hoy';
    }

    const timeLabel = appointmentDate.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // Obtener nombre abreviado del tipo de cita
    const appointmentType = appointmentTypes.find(t => t.id === appointment.typeOfAppointment);
    let typeName = appointmentType ? appointmentType.name : 'Desconocido';

    // Abreviar tipo de cita
    const typeShortNames = {
        'Asesoría académica': 'Asesoría',
        'Tutoría': 'Tutoría',
        'Orientación profesional': 'Orientación',
        'Bienestar universitario': 'Bienestar'
    };

    typeName = typeShortNames[typeName] || typeName;

    // HTML de la cita
    appointmentEl.innerHTML = `
        <div class="meta">
            <span>${dateLabel}, ${timeLabel}</span>
            <span class="type-badge">${typeName}</span>
        </div>
        <h3 class="title">${appointment.appointmentName}</h3>
        <p class="student">${appointment.userName}</p>
    `;

    return appointmentEl;
}

// Función para crear la vista semanal
function createWeekView(appointments) {
    // Estructura base de la vista semanal
    const weekContainer = document.createElement('div');
    weekContainer.className = 'calendar-week-view';

    // Obtener el primer día de la semana (domingo)
    const firstDayOfWeek = new Date(currentDate);
    firstDayOfWeek.setDate(currentDate.getDate() - currentDate.getDay());

    // Crear encabezados para los días de la semana
    const headerRow = document.createElement('div');
    headerRow.className = 'week-header-row';

    // Añadir celda vacía para la columna de horas
    const emptyHeader = document.createElement('div');
    emptyHeader.className = 'week-header-cell time-column';
    emptyHeader.textContent = 'Hora';
    headerRow.appendChild(emptyHeader);

    // Días de la semana
    const dayNames = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];

    // Crear un array con los 7 días de la semana actual
    const weekDays = [];
    for (let i = 0; i < 7; i++) {
        const day = new Date(firstDayOfWeek);
        day.setDate(firstDayOfWeek.getDate() + i);
        weekDays.push(day);

        // Crear celda de encabezado para este día
        const dayHeader = document.createElement('div');
        dayHeader.className = 'week-header-cell';

        // Resaltar el día actual
        if (day.toDateString() === new Date().toDateString()) {
            dayHeader.classList.add('today');
        }

        // Formato del encabezado: Nombre del día + número
        dayHeader.innerHTML = `
            <div class="day-name">${dayNames[day.getDay()]}</div>
            <div class="day-number">${day.getDate()}</div>
        `;

        headerRow.appendChild(dayHeader);
    }

    weekContainer.appendChild(headerRow);

    // Crear las filas de horas (de 8:00 a 18:00)
    const hourStart = 8;
    const hourEnd = 18;

    for (let hour = hourStart; hour <= hourEnd; hour++) {
        const timeRow = document.createElement('div');
        timeRow.className = 'week-time-row';

        // Celda de la hora
        const timeCell = document.createElement('div');
        timeCell.className = 'week-time-cell';
        timeCell.textContent = `${hour}:00`;
        timeRow.appendChild(timeCell);

        // Crear celdas para cada día en esta hora
        for (let i = 0; i < weekDays.length; i++) {
            const day = weekDays[i];
            const dayHourCell = document.createElement('div');
            dayHourCell.className = 'week-day-hour-cell';

            if (day.toDateString() === new Date().toDateString()) {
                dayHourCell.classList.add('today');
            }

            // Filtrar citas para este día y hora
            const hourAppointments = appointments.filter(appointment => {
                const appointmentDate = new Date(appointment.date);
                return (
                    appointmentDate.getDate() === day.getDate() &&
                    appointmentDate.getMonth() === day.getMonth() &&
                    appointmentDate.getFullYear() === day.getFullYear() &&
                    appointmentDate.getHours() === hour
                );
            });

            // Añadir citas a la celda
            if (hourAppointments.length > 0) {
                // Ordenar por minutos
                hourAppointments.sort((a, b) => {
                    const dateA = new Date(a.date);
                    const dateB = new Date(b.date);
                    return dateA.getMinutes() - dateB.getMinutes();
                });

                // Añadir cada cita
                hourAppointments.forEach(appointment => {
                    const appointmentEl = document.createElement('div');
                    const appointmentDate = new Date(appointment.date);
                    const minutes = appointmentDate.getMinutes().toString().padStart(2, '0');

                    // Determinar color según estado
                    let statusClass = 'scheduled';
                    if (appointment.status === 'completed') {
                        statusClass = 'completed';
                    } else if (appointment.status === 'cancelled') {
                        statusClass = 'cancelled';
                    }

                    appointmentEl.className = `week-appointment ${statusClass}`;
                    appointmentEl.innerHTML = `
                        <div class="appointment-time">${hour}:${minutes}</div>
                        <div class="appointment-title">${appointment.appointmentName}</div>
                        <div class="appointment-user">${appointment.userName}</div>
                    `;

                    // Añadir tooltip con detalles
                    appointmentEl.title = `${appointment.appointmentName} - ${appointment.userName}`;

                    dayHourCell.appendChild(appointmentEl);
                });
            }

            timeRow.appendChild(dayHourCell);
        }

        weekContainer.appendChild(timeRow);
    }

    calendarBody.appendChild(weekContainer);
}

// Función para crear la vista diaria
function createDayView(appointments) {
    // Estructura base de la vista diaria
    const dayContainer = document.createElement('div');
    dayContainer.className = 'calendar-day-view';

    // Crear las filas de horas (de 8:00 a 18:00)
    const hourStart = 8;
    const hourEnd = 18;

    for (let hour = hourStart; hour <= hourEnd; hour++) {
        const timeRow = document.createElement('div');
        timeRow.className = 'day-time-row';

        // Celda de la hora
        const timeCell = document.createElement('div');
        timeCell.className = 'day-time-cell';
        timeCell.textContent = `${hour}:00`;
        timeRow.appendChild(timeCell);

        // Celda para las citas de esta hora
        const appointmentsCell = document.createElement('div');
        appointmentsCell.className = 'day-appointments-cell';

        // Filtrar citas para este día y hora
        const hourAppointments = appointments.filter(appointment => {
            const appointmentDate = new Date(appointment.date);
            return (
                appointmentDate.getDate() === currentDate.getDate() &&
                appointmentDate.getMonth() === currentDate.getMonth() &&
                appointmentDate.getFullYear() === currentDate.getFullYear() &&
                appointmentDate.getHours() === hour
            );
        });

        // Añadir citas o mensaje de espacio libre
        if (hourAppointments.length > 0) {
            // Ordenar por minutos
            hourAppointments.sort((a, b) => {
                const dateA = new Date(a.date);
                const dateB = new Date(b.date);
                return dateA.getMinutes() - dateB.getMinutes();
            });

            // Añadir cada cita
            hourAppointments.forEach(appointment => {
                const appointmentEl = document.createElement('div');
                const appointmentDate = new Date(appointment.date);
                const minutes = appointmentDate.getMinutes().toString().padStart(2, '0');

                // Determinar color según estado
                let statusClass = 'scheduled';
                if (appointment.status === 'completed') {
                    statusClass = 'completed';
                } else if (appointment.status === 'cancelled') {
                    statusClass = 'cancelled';
                }

                appointmentEl.className = `day-appointment ${statusClass}`;

                // Obtener el tipo de cita
                const appointmentType = appointmentTypes.find(t => t.id === appointment.typeOfAppointment);
                const typeName = appointmentType ? appointmentType.name : 'Desconocido';

                appointmentEl.innerHTML = `
                    <div class="appointment-header">
                        <div class="appointment-time">${hour}:${minutes}</div>
                        <div class="appointment-status">${getStatusText(appointment.status)}</div>
                    </div>
                    <div class="appointment-title">${appointment.appointmentName}</div>
                    <div class="appointment-details">
                        <div class="appointment-user"><strong>Estudiante:</strong> ${appointment.userName}</div>
                        <div class="appointment-type"><strong>Tipo:</strong> ${typeName}</div>
                        ${appointment.details ? `<div class="appointment-notes"><strong>Detalles:</strong> ${appointment.details}</div>` : ''}
                    </div>
                `;

                appointmentsCell.appendChild(appointmentEl);
            });
        } else {
            // Horario libre
            const emptySlot = document.createElement('div');
            emptySlot.className = 'day-empty-slot';
            emptySlot.textContent = 'Horario disponible';
            appointmentsCell.appendChild(emptySlot);
        }

        timeRow.appendChild(appointmentsCell);
        dayContainer.appendChild(timeRow);
    }

    calendarBody.appendChild(dayContainer);
}

// Función para obtener texto de estado
function getStatusText(status) {
    switch (status) {
        case 'scheduled': return 'Programada';
        case 'completed': return 'Completada';
        case 'cancelled': return 'Cancelada';
        default: return 'Desconocido';
    }
}

// Cambiar entre vistas del calendario
function setCalendarView(view) {
    calendarView = view;

    // Actualizar clases de los botones
    btnMonthView.classList.toggle('active', view === 'month');
    btnWeekView.classList.toggle('active', view === 'week');
    btnDayView.classList.toggle('active', view === 'day');

    // Actualizar la clase del contenedor del calendario para aplicar estilos específicos
    calendarBody.className = 'calendar-body';
    calendarBody.classList.add(`view-${view}`);

    // Cambiar navegación según la vista
    if (view === 'month') {
        btnPrevMonth.onclick = () => {
            currentDate.setMonth(currentDate.getMonth() - 1);
            renderCalendar();
        };
        btnNextMonth.onclick = () => {
            currentDate.setMonth(currentDate.getMonth() + 1);
            renderCalendar();
        };
    } else if (view === 'week') {
        btnPrevMonth.onclick = () => {
            currentDate.setDate(currentDate.getDate() - 7);
            renderCalendar();
        };
        btnNextMonth.onclick = () => {
            currentDate.setDate(currentDate.getDate() + 7);
            renderCalendar();
        };
    } else if (view === 'day') {
        btnPrevMonth.onclick = () => {
            currentDate.setDate(currentDate.getDate() - 1);
            renderCalendar();
        };
        btnNextMonth.onclick = () => {
            currentDate.setDate(currentDate.getDate() + 1);
            renderCalendar();
        };
    }

    // Renderizar calendario según la vista
    renderCalendar();
}