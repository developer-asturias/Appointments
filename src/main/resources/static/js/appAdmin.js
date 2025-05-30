class CalendarApp {
    constructor() {
        this.currentDate = new Date();
        this.currentView = 'month';
        this.appointments = [];
        this.filteredAppointments = [];
        this.currentFilter = 'all';

        this.init();
    }

    async init() {
        await this.loadAppointments();
        this.setupEventListeners();
        this.updateCalendar();
        this.updateStats();
        this.updateUpcomingAppointments();
        this.setupModalEventListeners();
    }

    async loadAppointments() {
        try {
            // Calculate date range based on current view and date
            let startDate, endDate;

            if (this.currentView === 'month') {
                const year = this.currentDate.getFullYear();
                const month = this.currentDate.getMonth();
                startDate = new Date(year, month, 1);
                endDate = new Date(year, month + 1, 0);
            } else if (this.currentView === 'week') {
                startDate = new Date(this.currentDate);
                startDate.setDate(this.currentDate.getDate() - this.currentDate.getDay());
                endDate = new Date(startDate);
                endDate.setDate(startDate.getDate() + 6);
            } else {
                startDate = new Date(this.currentDate);
                endDate = new Date(this.currentDate);
            }

            // Format dates as expected by your backend: YYYY-MM-DD HH:MM:SS.SSS
            const formatDateForBackend = (date, isEndDate = false) => {
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');

                if (isEndDate) {
                    return `${year}-${month}-${day} 23:59:59.999`;
                } else {
                    return `${year}-${month}-${day} 00:00:00.000`;
                }
            };

            // Build the URL manually to avoid encoding issues with your backend
            const startParam = formatDateForBackend(startDate, false);
            const endParam = formatDateForBackend(endDate, true);
            const queryString = `start=${startParam}&end=${endParam}`;

            console.log('Requesting:', `/api/appointments/by-date-range?${queryString}`);

            // Fetch appointments from the API
            const response = await fetch(`/api/appointments/by-date-range?${queryString}`);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            // Handle empty response (204 No Content)
            let data;
            if (response.status === 204 || response.headers.get('content-length') === '0') {
                data = { appointments: [], totalAppointments: 0, statuses: {} };
            } else {
                data = await response.json();
            }

            console.log('API Response:', data); // Debug log

            // Transform API data to match expected format
            this.appointments = this.transformAppointments(data.appointments || []);
            this.appointmentStats = data.statuses || {};
            this.applyFilter();

            console.log('Transformed appointments:', this.appointments); // Debug log
        } catch (error) {
            console.error('Error loading appointments:', error);
            this.appointments = [];
            this.appointmentStats = {};
        }
    }

    // Transform API data structure to match what the calendar expects
    transformAppointments(apiAppointments) {
        return apiAppointments.map(apt => ({
            id: apt.id,
            datetime: apt.dateAppointment, // API usa 'dateAppointment', código espera 'datetime'
            patient: apt.student?.name || 'Sin nombre', // API usa 'student.name', código espera 'patient'
            type: apt.name || apt.typeOfAppointmentName || 'Cita general', // API usa 'name', código espera 'type'
            status: this.mapStatus(apt.status || 'ACTIVE'), // Mapear status de API a formato esperado
            details: apt.details || '',
            email: apt.student?.email || '',
            // Store the original data for the modal
            original: apt
        }));
    }

    // Map API status to calendar expected format
    mapStatus(apiStatus) {
        const statusMap = {
            'ACTIVE': 'scheduled',
            'SCHEDULED': 'scheduled',
            'COMPLETED': 'completed',
            'CANCELLED': 'cancelled'
        };
        return statusMap[apiStatus] || 'scheduled';
    }

    setupEventListeners() {
        // View buttons
        document.querySelectorAll('.view-btn').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                await this.switchView(e.target.dataset.view);
            });
        });

        // Navigation buttons
        document.getElementById('prev-btn').addEventListener('click', async () => {
            await this.navigatePrevious();
        });

        document.getElementById('next-btn').addEventListener('click', async () => {
            await this.navigateNext();
        });

        // Filter buttons
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                this.setFilter(e.target.dataset.status);
            });
        });

        // Search functionality
        const searchInput = document.querySelector('.search-input');
        const searchBtn = document.querySelector('.search-btn');

        searchBtn.addEventListener('click', () => {
            this.performSearch(searchInput.value);
        });

        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.performSearch(searchInput.value);
            }
        });
    }

    async switchView(view) {
        this.currentView = view;

        // Update button states
        document.querySelectorAll('.view-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.querySelector(`[data-view="${view}"]`).classList.add('active');

        // Update calendar views
        document.querySelectorAll('.calendar-view').forEach(viewEl => {
            viewEl.classList.remove('active');
        });
        document.getElementById(`${view}-view`).classList.add('active');

        // Reload appointments for new view
        await this.loadAppointments();
        this.updateCalendar();
        this.updateStats();
    }

    async navigatePrevious() {
        switch (this.currentView) {
            case 'day':
                this.currentDate.setDate(this.currentDate.getDate() - 1);
                break;
            case 'week':
                this.currentDate.setDate(this.currentDate.getDate() - 7);
                break;
            case 'month':
                this.currentDate.setMonth(this.currentDate.getMonth() - 1);
                break;
        }
        await this.loadAppointments();
        this.updateCalendar();
        this.updateStats();
    }

    async navigateNext() {
        switch (this.currentView) {
            case 'day':
                this.currentDate.setDate(this.currentDate.getDate() + 1);
                break;
            case 'week':
                this.currentDate.setDate(this.currentDate.getDate() + 7);
                break;
            case 'month':
                this.currentDate.setMonth(this.currentDate.getMonth() + 1);
                break;
        }
        await this.loadAppointments();
        this.updateCalendar();
        this.updateStats();
    }

    setFilter(status) {
        this.currentFilter = status;

        // Update button states
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.querySelector(`[data-status="${status}"]`).classList.add('active');

        this.applyFilter();
        this.updateCalendar();
        this.updateStats();
    }

    applyFilter() {
        if (this.currentFilter === 'all') {
            this.filteredAppointments = [...this.appointments];
        } else {
            this.filteredAppointments = this.appointments.filter(apt =>
                apt.status === this.currentFilter
            );
        }
    }

    updateCalendar() {
        this.updatePeriodLabel();

        switch (this.currentView) {
            case 'month':
                this.renderMonthView();
                break;
            case 'week':
                this.renderWeekView();
                break;
            case 'day':
                this.renderDayView();
                break;
        }
    }

    updatePeriodLabel() {
        const label = document.getElementById('current-period');
        const months = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        switch (this.currentView) {
            case 'day':
                label.textContent = this.currentDate.toLocaleDateString('es-ES', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                });
                break;
            case 'week':
                const weekStart = new Date(this.currentDate);
                weekStart.setDate(this.currentDate.getDate() - this.currentDate.getDay());
                const weekEnd = new Date(weekStart);
                weekEnd.setDate(weekStart.getDate() + 6);

                label.textContent = `Semana del ${weekStart.getDate()} - ${weekEnd.getDate()} de ${months[weekStart.getMonth()]} ${weekStart.getFullYear()}`;
                break;
            case 'month':
                label.textContent = `${months[this.currentDate.getMonth()]} ${this.currentDate.getFullYear()}`;
                break;
        }
    }

    renderMonthView() {
        const calendarDays = document.getElementById('calendar-days');
        calendarDays.innerHTML = '';

        const year = this.currentDate.getFullYear();
        const month = this.currentDate.getMonth();

        // Get first day of month and number of days
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDayOfWeek = firstDay.getDay();

        // Add previous month's trailing days
        const prevMonth = new Date(year, month, 0);
        const prevMonthDays = prevMonth.getDate();

        for (let i = startingDayOfWeek - 1; i >= 0; i--) {
            const dayNum = prevMonthDays - i;
            const dayElement = this.createMonthDayElement(dayNum, true);
            calendarDays.appendChild(dayElement);
        }

        // Add current month's days
        for (let day = 1; day <= daysInMonth; day++) {
            const dayElement = this.createMonthDayElement(day, false);
            const dayDate = new Date(year, month, day);

            // Add appointments for this day
            const dayAppointments = this.getAppointmentsForDate(dayDate);
            dayAppointments.forEach(apt => {
                const aptElement = this.createAppointmentElement(apt, 'month');
                dayElement.appendChild(aptElement);
            });

            // Highlight day if it has appointments
            if (dayAppointments.length > 0) {
                dayElement.classList.add('has-appointments');
            }

            calendarDays.appendChild(dayElement);
        }

        // Add next month's leading days
        const totalCells = calendarDays.children.length;
        const remainingCells = 42 - totalCells; // 6 rows × 7 days
        for (let day = 1; day <= remainingCells; day++) {
            const dayElement = this.createMonthDayElement(day, true);
            calendarDays.appendChild(dayElement);
        }
    }

    createMonthDayElement(day, isOtherMonth) {
        const dayElement = document.createElement('div');
        dayElement.className = `calendar-day ${isOtherMonth ? 'other-month' : ''}`;

        const dayNumber = document.createElement('div');
        dayNumber.className = 'day-number';
        dayNumber.textContent = day;

        dayElement.appendChild(dayNumber);
        return dayElement;
    }

    renderWeekView() {
        // Implementation for week view
        console.log("Week view not fully implemented");
    }

    renderDayView() {
        // Implementation for day view
        console.log("Day view not fully implemented");
    }

    getAppointmentsForDate(date) {
        return this.filteredAppointments.filter(apt => {
            const aptDate = new Date(apt.datetime);
            return aptDate.getDate() === date.getDate() &&
                   aptDate.getMonth() === date.getMonth() &&
                   aptDate.getFullYear() === date.getFullYear();
        });
    }

    createAppointmentElement(apt, view) {
        const element = document.createElement('div');
        element.className = `appointment-item ${apt.status}`;
        element.dataset.appointmentId = apt.id;

        // Time
        const time = document.createElement('span');
        time.className = 'appointment-time';
        const aptDate = new Date(apt.datetime);
        time.textContent = aptDate.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });

        // Patient name
        const patient = document.createElement('span');
        patient.className = 'appointment-patient';
        patient.textContent = apt.patient || 'Sin nombre';

        // Appointment type
        const type = document.createElement('span');
        type.className = 'appointment-type';
        type.textContent = apt.type || 'Consulta General';

        element.appendChild(time);
        element.appendChild(patient);
        element.appendChild(type);

        // Add click event to open the modal
        element.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();
            this.openAppointmentModal(apt.id);
        });

        return element;
    }

    updateStats() {
        // Update statistics cards
        const totalElement = document.getElementById('total-appointments');
        const scheduledElement = document.getElementById('scheduled-appointments');
        const completedElement = document.getElementById('completed-appointments');
        const cancelledElement = document.getElementById('cancelled-appointments');

        totalElement.textContent = this.appointments.length || 0;

        scheduledElement.textContent =
            this.appointments.filter(apt => apt.status === 'scheduled').length || 0;

        completedElement.textContent =
            this.appointments.filter(apt => apt.status === 'completed').length || 0;

        cancelledElement.textContent =
            this.appointments.filter(apt => apt.status === 'cancelled').length || 0;
    }

    updateUpcomingAppointments() {
        const upcomingList = document.getElementById('upcoming-list');
        upcomingList.innerHTML = '';

        // Get today's date
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        // Filter appointments that are from today onwards and sort by date
        const upcomingAppointments = this.appointments
            .filter(apt => {
                const aptDate = new Date(apt.datetime);
                return aptDate >= today;
            })
            .sort((a, b) => new Date(a.datetime) - new Date(b.datetime))
            .slice(0, 5); // Only show 5 upcoming appointments

        if (upcomingAppointments.length === 0) {
            const noAppointments = document.createElement('p');
            noAppointments.className = 'text-gray-500 text-center';
            noAppointments.textContent = 'No hay citas próximas programadas.';
            upcomingList.appendChild(noAppointments);
            return;
        }

        // Create elements for each upcoming appointment
        upcomingAppointments.forEach(apt => {
            const element = document.createElement('div');
            element.className = `upcoming-item ${apt.status}`;
            element.dataset.appointmentId = apt.id;

            const date = new Date(apt.datetime);
            const dateString = date.toLocaleDateString('es-ES', {
                weekday: 'short',
                month: 'short',
                day: 'numeric'
            });
            const timeString = date.toLocaleTimeString('es-ES', {
                hour: '2-digit',
                minute: '2-digit'
            });

            const time = document.createElement('div');
            time.className = 'upcoming-time';
            time.textContent = `${dateString} - ${timeString}`;

            const patient = document.createElement('div');
            patient.className = 'upcoming-patient';
            patient.textContent = apt.patient;

            const type = document.createElement('div');
            type.className = 'upcoming-type';
            type.textContent = apt.type;

            element.appendChild(time);
            element.appendChild(patient);
            element.appendChild(type);

            // Add click event to open the modal
            element.addEventListener('click', () => {
                this.openAppointmentModal(apt.id);
            });

            upcomingList.appendChild(element);
        });
    }

    performSearch(query) {
        if (!query.trim()) return;

        // Filter appointments by patient name or type
        const results = this.appointments.filter(apt =>
            apt.patient.toLowerCase().includes(query.toLowerCase()) ||
            apt.type.toLowerCase().includes(query.toLowerCase())
        );

        // Update search results display
        const searchResults = document.querySelector('.search-results');
        searchResults.innerHTML = '';

        if (results.length === 0) {
            const noResults = document.createElement('p');
            noResults.textContent = `No se encontraron resultados para "${query}".`;
            searchResults.appendChild(noResults);
            return;
        }

        results.forEach(apt => {
            const element = document.createElement('div');
            element.className = `appointment-item ${apt.status} mb-2`;
            element.dataset.appointmentId = apt.id;

            const date = new Date(apt.datetime);
            const dateString = date.toLocaleDateString('es-ES', {
                month: 'short',
                day: 'numeric'
            });
            const timeString = date.toLocaleTimeString('es-ES', {
                hour: '2-digit',
                minute: '2-digit'
            });

            element.innerHTML = `
                <span class="appointment-time">${dateString} - ${timeString}</span>
                <span class="appointment-patient">${apt.patient}</span>
                <span class="appointment-type">${apt.type}</span>
            `;

            // Add click event to open the modal
            element.addEventListener('click', () => {
                this.openAppointmentModal(apt.id);
            });

            searchResults.appendChild(element);
        });
    }

    // Modal functionality
    setupModalEventListeners() {
        const modal = document.getElementById('appointment-modal');
        const closeBtn = modal.querySelector('.close-modal');
        const retryBtn = document.getElementById('retry-button');
        const editTeamsBtn = document.getElementById('edit-teams-link');
        const saveTeamsBtn = document.getElementById('save-teams-link');
        const cancelTeamsBtn = document.getElementById('cancel-teams-link');
        const joinMeetingBtn = document.getElementById('join-meeting-btn');
        const completeAppointmentBtn = document.getElementById('complete-appointment-btn');
        const cancelAppointmentBtn = document.getElementById('cancel-appointment-btn');

        // Close modal when clicking the X button
        closeBtn.addEventListener('click', () => {
            this.closeModal();
        });

        // Close modal when clicking outside of it
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                this.closeModal();
            }
        });

        // Close modal with escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.style.display === 'block') {
                this.closeModal();
            }
        });

        // Retry button for error state
        retryBtn.addEventListener('click', () => {
            const appointmentId = modal.dataset.appointmentId;
            if (appointmentId) {
                this.loadAppointmentDetails(parseInt(appointmentId));
            }
        });

        // Edit Teams link button
        editTeamsBtn.addEventListener('click', () => {
            const viewDiv = document.getElementById('teams-link-view');
            const editDiv = document.getElementById('teams-link-edit');
            const linkInput = document.getElementById('teams-link-input');
            const teamsLink = document.getElementById('teams-link');

            // Set the current link value to the input
            linkInput.value = teamsLink.href !== '#' ? teamsLink.href : '';

            // Toggle the view/edit modes
            viewDiv.style.display = 'none';
            editDiv.style.display = 'block';

            // Focus the input
            linkInput.focus();
        });

        // Save Teams link button
        saveTeamsBtn.addEventListener('click', () => {
            this.saveTeamsLink();
        });

        // Cancel Teams link edit button
        cancelTeamsBtn.addEventListener('click', () => {
            const viewDiv = document.getElementById('teams-link-view');
            const editDiv = document.getElementById('teams-link-edit');

            viewDiv.style.display = 'block';
            editDiv.style.display = 'none';
        });

        // Join meeting button
        joinMeetingBtn.addEventListener('click', () => {
            const teamsLink = document.getElementById('teams-link');
            if (teamsLink.href && teamsLink.href !== '#') {
                window.open(teamsLink.href, '_blank');
            } else {
                alert('No hay enlace de Teams disponible para esta cita.');
            }
        });

        // Complete appointment button
        completeAppointmentBtn.addEventListener('click', () => {
            const appointmentId = modal.dataset.appointmentId;
            if (appointmentId) {
                this.updateAppointmentStatus(parseInt(appointmentId), 'COMPLETED');
            }
        });

        // Cancel appointment button
        cancelAppointmentBtn.addEventListener('click', () => {
            const appointmentId = modal.dataset.appointmentId;
            if (appointmentId) {
                if (confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
                    this.updateAppointmentStatus(parseInt(appointmentId), 'CANCELLED');
                }
            }
        });
    }

    // Save Teams link
    saveTeamsLink() {
        const viewDiv = document.getElementById('teams-link-view');
        const editDiv = document.getElementById('teams-link-edit');
        const linkInput = document.getElementById('teams-link-input');
        const teamsLink = document.getElementById('teams-link');
        const teamsLinkText = document.getElementById('teams-link-text');
        const modal = document.getElementById('appointment-modal');
        const appointmentId = parseInt(modal.dataset.appointmentId);

        // Get the input value
        const newLink = linkInput.value.trim();

        if (newLink) {
            // Update the link
            teamsLink.href = newLink;
            teamsLinkText.textContent = this.getDisplayUrl(newLink);

            // Update in the backend (would use a real API call in production)
            this.updateTeamsLink(appointmentId, newLink);
        } else {
            // Reset to no link
            teamsLink.href = '#';
            teamsLinkText.textContent = 'No hay enlace disponible';
        }

        // Back to view mode
        viewDiv.style.display = 'block';
        editDiv.style.display = 'none';
    }

    // Update Teams link in the backend
    async updateTeamsLink(appointmentId, teamsLink) {
        try {
            // In a real application, you would make an API call to update the Teams link
            console.log(`Updating Teams link for appointment ${appointmentId} to: ${teamsLink}`);

            // We would normally use something like:
            // await fetch(`/api/appointments/${appointmentId}/update-teams-link`, {
            //     method: 'PATCH',
            //     headers: { 'Content-Type': 'application/json' },
            //     body: JSON.stringify({ linkTeams: teamsLink })
            // });

            // For now, update the local copy
            const appointment = this.appointments.find(apt => apt.id === appointmentId);
            if (appointment && appointment.original) {
                appointment.original.linkTeams = teamsLink;
            }
        } catch (error) {
            console.error('Error updating Teams link:', error);
            alert('No se pudo actualizar el enlace de Teams. Por favor intente de nuevo.');
        }
    }

    // Update appointment status
    async updateAppointmentStatus(appointmentId, newStatus) {
        try {
            // In a real application, you would make an API call to update the status
            console.log(`Updating appointment ${appointmentId} status to: ${newStatus}`);

            // We would normally use something like:
            // await fetch(`/api/appointments/${appointmentId}/update-status`, {
            //     method: 'PATCH',
            //     headers: { 'Content-Type': 'application/json' },
            //     body: JSON.stringify({ status: newStatus })
            // });

            // For now, update the local copy and close the modal
            const appointment = this.appointments.find(apt => apt.id === appointmentId);
            if (appointment) {
                appointment.status = this.mapStatus(newStatus);
                if (appointment.original) {
                    appointment.original.status = newStatus;
                }

                // Close the modal
                this.closeModal();

                // Refresh the calendar view
                this.applyFilter();
                this.updateCalendar();
                this.updateStats();
                this.updateUpcomingAppointments();

                // Show success message
                const statusAction = newStatus === 'COMPLETED' ? 'completada' : 'cancelada';
                alert(`La cita ha sido ${statusAction} exitosamente.`);
            }
        } catch (error) {
            console.error('Error updating appointment status:', error);
            alert('No se pudo actualizar el estado de la cita. Por favor intente de nuevo.');
        }
    }

    // Get a readable URL for display
    getDisplayUrl(url) {
        try {
            if (!url) return 'No hay enlace disponible';

            if (url.startsWith('http')) {
                const urlObj = new URL(url);
                return urlObj.hostname + urlObj.pathname.substring(0, 15) + (urlObj.pathname.length > 15 ? '...' : '');
            }

            return url.length > 30 ? url.substring(0, 30) + '...' : url;
        } catch (e) {
            return url.length > 30 ? url.substring(0, 30) + '...' : url;
        }
    }

    openAppointmentModal(appointmentId) {
        const modal = document.getElementById('appointment-modal');
        modal.dataset.appointmentId = appointmentId;
        modal.style.display = 'block';

        // Reset modal state
        document.getElementById('appointment-loading').style.display = 'block';
        document.getElementById('appointment-error').style.display = 'none';
        document.getElementById('appointment-details').style.display = 'none';

        // Load appointment details
        this.loadAppointmentDetails(appointmentId);
    }

    closeModal() {
        const modal = document.getElementById('appointment-modal');
        modal.style.display = 'none';
    }

    async loadAppointmentDetails(appointmentId) {
        try {
            document.getElementById('appointment-loading').style.display = 'block';
            document.getElementById('appointment-error').style.display = 'none';
            document.getElementById('appointment-details').style.display = 'none';

            // Fetch appointment details from API
            const response = await fetch(`/api/appointments/details/${appointmentId}`);
            if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`);
            const data = await response.json();

            console.log('Appointment details response:', data);

            this.displayAppointmentDetails(data);
        } catch (error) {
            console.error('Error loading appointment details:', error);
            document.getElementById('appointment-loading').style.display = 'none';
            document.getElementById('appointment-error').style.display = 'block';
            const errorMsg = document.querySelector('.error-message');
            errorMsg.textContent = error.message || 'No se pudo cargar la información de esta cita.';
        }
    }

    getReverseStatus(status) {
        const statusMap = {
            'scheduled': 'ACTIVE',
            'completed': 'COMPLETED',
            'cancelled': 'CANCELLED'
        };
        return statusMap[status] || 'ACTIVE';
    }

    displayAppointmentDetails(data) {
        document.getElementById('appointment-loading').style.display = 'none';
        document.getElementById('appointment-error').style.display = 'none';
        document.getElementById('appointment-details').style.display = 'block';

        // Status badge
        const statusBadge = document.getElementById('appointment-status');
        statusBadge.textContent = this.getStatusLabel(data.appointmentStatus);
        statusBadge.className = `status-badge ${this.mapStatus(data.appointmentStatus)}`;

        // Student info
        document.getElementById('appointment-student').textContent = data.nameStudent || 'Sin nombre';
        document.getElementById('appointment-email').textContent = data.email || 'No disponible';

        // Appointment details
        const appointmentDate = new Date(data.appointmentDate);
        document.getElementById('appointment-date').textContent = this.formatDate(appointmentDate);
        document.getElementById('appointment-time').textContent = this.formatTime(appointmentDate);
        document.getElementById('appointment-type').textContent = data.appointmentName || 'Consulta General';
        document.getElementById('appointment-phone').textContent = data.phone || 'No disponible';

        // Teams link
        const teamsLink = document.getElementById('teams-link');
        const teamsLinkText = document.getElementById('teams-link-text');

        if (data.linkTeams) {
            teamsLink.href = data.linkTeams;
            teamsLinkText.textContent = this.getDisplayUrl(data.linkTeams);
        } else {
            teamsLink.href = '#';
            teamsLinkText.textContent = 'No hay enlace disponible';
        }

        // Description
        document.getElementById('appointment-description').textContent =
            data.appointmentDescription || 'No hay descripción disponible para esta cita.';

        // Disable action buttons based on status
        const joinMeetingBtn = document.getElementById('join-meeting-btn');
        const completeAppointmentBtn = document.getElementById('complete-appointment-btn');
        const cancelAppointmentBtn = document.getElementById('cancel-appointment-btn');

        // Only enabled buttons for active appointments
        const isActive = data.appointmentStatus === 'ACTIVE' || data.appointmentStatus === 'SCHEDULED';

        joinMeetingBtn.disabled = !isActive;
        completeAppointmentBtn.disabled = !isActive;
        cancelAppointmentBtn.disabled = !isActive;

        if (!isActive) {
            joinMeetingBtn.classList.add('disabled');
            completeAppointmentBtn.classList.add('disabled');
            cancelAppointmentBtn.classList.add('disabled');
        } else {
            joinMeetingBtn.classList.remove('disabled');
            completeAppointmentBtn.classList.remove('disabled');
            cancelAppointmentBtn.classList.remove('disabled');
        }
    }

    getStatusLabel(status) {
        const statusLabels = {
            'ACTIVE': 'Programada',
            'SCHEDULED': 'Programada',
            'COMPLETED': 'Completada',
            'CANCELLED': 'Cancelada'
        };
        return statusLabels[status] || 'Programada';
    }

    formatDate(date) {
        return date.toLocaleDateString('es-ES', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }

    formatTime(date) {
        return date.toLocaleTimeString('es-ES', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}

// Initialize the calendar app
document.addEventListener('DOMContentLoaded', () => {
    window.calendarApp = new CalendarApp();
});