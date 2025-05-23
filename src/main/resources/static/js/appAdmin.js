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
            details: apt.details || ''
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
        const prevMonth = new Date(year, month - 1, 0);
        for (let i = startingDayOfWeek - 1; i >= 0; i--) {
            const dayNum = prevMonth.getDate() - i;
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
        const weekHeader = document.getElementById('week-header');
        const weekGrid = document.getElementById('week-grid');

        weekHeader.innerHTML = '';
        weekGrid.innerHTML = '';

        // Calculate week start (Sunday)
        const weekStart = new Date(this.currentDate);
        weekStart.setDate(this.currentDate.getDate() - this.currentDate.getDay());

        // Create header
        const timeHeader = document.createElement('div');
        timeHeader.textContent = 'Hora';
        weekHeader.appendChild(timeHeader);

        const days = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
        for (let i = 0; i < 7; i++) {
            const dayDate = new Date(weekStart);
            dayDate.setDate(weekStart.getDate() + i);

            const dayHeader = document.createElement('div');
            dayHeader.innerHTML = `
                <div style="font-weight: 600;">${days[i]}</div>
                <div style="font-size: 0.875rem; color: #6b7280;">${dayDate.getDate()}</div>
            `;
            weekHeader.appendChild(dayHeader);
        }

        // Create time slots (8 AM to 6 PM)
        for (let hour = 8; hour <= 18; hour++) {
            // Hour label
            const hourLabel = document.createElement('div');
            hourLabel.className = 'hour-label';
            hourLabel.textContent = `${hour}:00`;
            weekGrid.appendChild(hourLabel);

            // Day slots for this hour
            for (let day = 0; day < 7; day++) {
                const dayDate = new Date(weekStart);
                dayDate.setDate(weekStart.getDate() + day);
                dayDate.setHours(hour, 0, 0, 0);

                const slot = document.createElement('div');
                slot.className = 'week-day-slot';

                // Add appointments for this hour
                const appointments = this.getAppointmentsForDateTime(dayDate);
                appointments.forEach(apt => {
                    const aptElement = this.createAppointmentElement(apt, 'week');
                    slot.appendChild(aptElement);
                });

                weekGrid.appendChild(slot);
            }
        }
    }

    renderDayView() {
        const dayHeader = document.getElementById('day-header');
        const daySchedule = document.getElementById('day-schedule');

        dayHeader.innerHTML = `
            <h3>${this.currentDate.toLocaleDateString('es-ES', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            })}</h3>
        `;

        daySchedule.innerHTML = '';

        // Create hour slots (8 AM to 6 PM)
        for (let hour = 8; hour <= 18; hour++) {
            const hourSlot = document.createElement('div');
            hourSlot.className = 'hour-slot';

            // Hour label
            const hourLabel = document.createElement('div');
            hourLabel.className = 'hour-label';
            hourLabel.textContent = `${hour}:00`;

            // Hour content
            const hourContent = document.createElement('div');
            hourContent.className = 'hour-content';

            // Add appointments for this hour
            const slotDate = new Date(this.currentDate);
            slotDate.setHours(hour, 0, 0, 0);

            const appointments = this.getAppointmentsForDateTime(slotDate);
            appointments.forEach(apt => {
                const aptElement = this.createAppointmentElement(apt, 'day');
                hourContent.appendChild(aptElement);
            });

            hourSlot.appendChild(hourLabel);
            hourSlot.appendChild(hourContent);
            daySchedule.appendChild(hourSlot);
        }
    }

    getAppointmentsForDate(date) {
        return this.filteredAppointments.filter(apt => {
            const aptDate = new Date(apt.datetime);
            return aptDate.toDateString() === date.toDateString();
        });
    }

    getAppointmentsForDateTime(dateTime) {
        return this.filteredAppointments.filter(apt => {
            const aptDate = new Date(apt.datetime);
            return aptDate.toDateString() === dateTime.toDateString() &&
                   aptDate.getHours() === dateTime.getHours();
        });
    }

    createAppointmentElement(appointment, viewType) {
        const element = document.createElement('div');
        element.className = `appointment-item ${appointment.status}`;

        const time = new Date(appointment.datetime);
        const timeStr = time.toLocaleTimeString('es-ES', {
            hour: '2-digit',
            minute: '2-digit'
        });

        if (viewType === 'month') {
            element.innerHTML = `
                <span class="appointment-time">${timeStr}</span>
                <span class="appointment-patient">${appointment.patient}</span>
                <span class="appointment-type">${appointment.type}</span>
            `;
        } else {
            element.innerHTML = `
                <div style="font-weight: 600; margin-bottom: 0.25rem;">${timeStr} - ${appointment.patient}</div>
                <div style="font-size: 0.75rem; color: #6b7280;">${appointment.type}</div>
            `;
        }

        return element;
    }

    updateStats() {
        // Use backend stats if available, otherwise calculate from appointments
        if (this.appointmentStats && Object.keys(this.appointmentStats).length > 0) {
            // Map API status names to display values
            const scheduled = (this.appointmentStats.ACTIVE || 0) + (this.appointmentStats.SCHEDULED || 0);
            const completed = this.appointmentStats.COMPLETED || 0;
            const cancelled = this.appointmentStats.CANCELLED || 0;
            const total = scheduled + completed + cancelled;

            document.getElementById('total-appointments').textContent = total;
            document.getElementById('scheduled-appointments').textContent = scheduled;
            document.getElementById('completed-appointments').textContent = completed;
            document.getElementById('cancelled-appointments').textContent = cancelled;
        } else {
            // Fallback to calculating from appointments array
            const total = this.appointments.length;
            const scheduled = this.appointments.filter(apt => apt.status === 'scheduled').length;
            const completed = this.appointments.filter(apt => apt.status === 'completed').length;
            const cancelled = this.appointments.filter(apt => apt.status === 'cancelled').length;

            document.getElementById('total-appointments').textContent = total;
            document.getElementById('scheduled-appointments').textContent = scheduled;
            document.getElementById('completed-appointments').textContent = completed;
            document.getElementById('cancelled-appointments').textContent = cancelled;
        }
    }

    updateUpcomingAppointments() {
        const upcomingList = document.getElementById('upcoming-list');
        upcomingList.innerHTML = '';

        // Get upcoming appointments (next 7 days)
        const now = new Date();
        const nextWeek = new Date();
        nextWeek.setDate(now.getDate() + 7);

        const upcoming = this.appointments
            .filter(apt => {
                const aptDate = new Date(apt.datetime);
                return aptDate >= now && aptDate <= nextWeek && apt.status === 'scheduled';
            })
            .sort((a, b) => new Date(a.datetime) - new Date(b.datetime))
            .slice(0, 5);

        if (upcoming.length === 0) {
            upcomingList.innerHTML = '<p style="color: #6b7280; text-align: center;">No hay citas próximas</p>';
            return;
        }

        upcoming.forEach(apt => {
            const item = document.createElement('div');
            item.className = `upcoming-item ${apt.status}`;

            const aptDate = new Date(apt.datetime);
            const timeStr = aptDate.toLocaleString('es-ES', {
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });

            item.innerHTML = `
                <div class="upcoming-time">${timeStr}</div>
                <div class="upcoming-title">${apt.patient}</div>
                <div class="upcoming-type">${apt.type}</div>
            `;

            upcomingList.appendChild(item);
        });
    }

    performSearch(query) {
        const searchResults = document.querySelector('.search-results');

        if (!query.trim()) {
            searchResults.innerHTML = '<p>Ingrese un término de búsqueda para encontrar citas.</p>';
            return;
        }

        const results = this.appointments.filter(apt =>
            apt.patient.toLowerCase().includes(query.toLowerCase()) ||
            apt.type.toLowerCase().includes(query.toLowerCase())
        );

        if (results.length === 0) {
            searchResults.innerHTML = '<p>No se encontraron citas que coincidan con la búsqueda.</p>';
            return;
        }

        searchResults.innerHTML = '';
        results.slice(0, 5).forEach(apt => {
            const item = document.createElement('div');
            item.className = `upcoming-item ${apt.status}`;

            const aptDate = new Date(apt.datetime);
            const timeStr = aptDate.toLocaleString('es-ES', {
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });

            item.innerHTML = `
                <div class="upcoming-time">${timeStr}</div>
                <div class="upcoming-title">${apt.patient}</div>
                <div class="upcoming-type">${apt.type}</div>
            `;

            searchResults.appendChild(item);
        });

        if (results.length > 5) {
            const moreResults = document.createElement('p');
            moreResults.style.color = '#6b7280';
            moreResults.style.fontSize = '0.875rem';
            moreResults.textContent = `... y ${results.length - 5} resultados más`;
            searchResults.appendChild(moreResults);
        }
    }
}

// Initialize the calendar when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new CalendarApp();
});