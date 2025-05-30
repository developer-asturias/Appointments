/**
 * Main Application JavaScript
 * Handles form validation, submission, and UI interactions
 */
document.addEventListener('DOMContentLoaded', () => {
    // Global variables
    let appointmentTypes = [];
    let programs = [];
    const toast = new bootstrap.Toast(document.getElementById('notification-toast'));

    // Initialize form with data and validation
    init();

    /**
     * Initialize the application
     */
    async function init() {
        await loadFormData();
        setupEventListeners();
        setMinDate();
    }

    /**
     * Load dropdowns with data from API
     */
    async function loadFormData() {
        try {
            showLoadingState(true);

            // Load appointment types
            appointmentTypes = await API.getAppointmentTypes();
            populateSelect('typeOfAppointmentId', appointmentTypes);

            // Load programs
            programs = await API.getPrograms();
            populateSelect('programId', programs);

            showLoadingState(false);
        } catch (error) {
            showNotification(error.message, 'error');
            showLoadingState(false);
        }
    }

    /**
     * Populate a select dropdown with options
     * @param {string} selectId - The ID of the select element
     * @param {Array} options - Array of options with id and name properties
     */
    function populateSelect(selectId, options) {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="" selected disabled>Seleccione una opción</option>';

        options.forEach(option => {
            const optionElement = document.createElement('option');
            optionElement.value = option.id;
            optionElement.textContent = option.name;
            select.appendChild(optionElement);
        });
    }

    /**
     * Set up all event listeners
     */
    function setupEventListeners() {
        // Form submission
        document.getElementById('appointment-form').addEventListener('submit', handleAppointmentSubmit);

        // Search by document
        document.getElementById('btn-search-document').addEventListener('click', handleSearchByDocument);

        // Input validation on blur, change and input
        document.querySelectorAll('input, select').forEach(element => {
            element.addEventListener('blur', () => validateField(element));
            element.addEventListener('change', () => validateField(element));
            element.addEventListener('input', () => validateField(element));
        });
    }

    /**
     * Set minimum datetime for the date picker (now + 1 hour)
     */
    function setMinDate() {
        const now = new Date();
        now.setHours(now.getHours() + 1);

        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');

        const minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
        document.getElementById('date').setAttribute('min', minDateTime);
    }

    /**
     * Handle appointment form submission
     * @param {Event} event - The submit event
     */
    async function handleAppointmentSubmit(event) {
        event.preventDefault();

        // Validate all fields
        const form = event.target;
        const isValid = validateForm(form);

        if (!isValid) {
            showNotification('Por favor corrija los errores en el formulario.', 'error');
            return;
        }

        // Prepare form data
        const formData = {
            userName: form.userName.value.trim(),
            userEmail: form.userEmail.value.trim(),
            phone: form.phone.value.trim(),
            numberDocument: form.numberDocument.value.trim(),
            programId: form.programId.value,
            date: new Date(form.date.value).toISOString(),
            appointmentName: form.appointmentName.value.trim(),
            typeOfAppointmentId: form.typeOfAppointmentId.value,
            details: form.details.value.trim()
        };

        try {
            showLoadingState(true);
            const response = await API.createAppointment(formData);
            showNotification('¡Cita registrada con éxito!', 'success');
            form.reset();
            showLoadingState(false);
        } catch (error) {
            showNotification(error.message, 'error');
            showLoadingState(false);
        }
    }

    /**
     * Handle search by document
     */
    async function handleSearchByDocument() {
        const documentInput = document.getElementById('searchDocument');
        const documentNumber = documentInput.value.trim();

        if (!documentNumber) {
            showNotification('Por favor ingrese un número de documento.', 'error');
            return;
        }

        try {
            showLoadingState(true);
            const appointments = await API.searchAppointmentsByDocument(documentNumber);
            displayAppointments(appointments);
            showLoadingState(false);
        } catch (error) {
            showNotification(error.message, 'error');
            showLoadingState(false);
        }
    }

    /**
     * Display appointments in the search results section
     * @param {Array} appointments - Array of appointment objects
     */
    function displayAppointments(appointments) {
        const container = document.getElementById('appointments-container');
        const emptyMessage = document.getElementById('empty-appointments');
        const appointmentsList = document.getElementById('appointments-list');
        const resultsCard = document.getElementById('search-results-card');

        // Show the results card
        resultsCard.style.display = 'block';

        // Clear previous results
        appointmentsList.innerHTML = '';

        if (!appointments || appointments.length === 0) {
            emptyMessage.style.display = 'block';
            emptyMessage.textContent = 'No se encontraron citas para este documento.';
            return;
        }

        // Hide empty message and show appointments
        emptyMessage.style.display = 'none';

        appointments.forEach(appointment => {
            const appointmentDate = new Date(appointment.date);
            const formattedDate = appointmentDate.toLocaleDateString('es-ES', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });

            let statusClass = 'badge-scheduled';
            let statusText = 'Programada';

            if (appointment.status === 'COMPLETED') {
                statusClass = 'badge-completed';
                statusText = 'Completada';
            } else if (appointment.status === 'CANCELLED') {
                statusClass = 'badge-cancelled';
                statusText = 'Cancelada';
            }

            const appointmentHtml = `
                <div class="appointment-item" data-id="${appointment.id}">
                    <div class="appointment-header">
                        <div>
                            <div class="appointment-title">${appointment.appointmentName}</div>
                            <div class="appointment-type">${getAppointmentTypeName(appointment.typeOfAppointmentId)}</div>
                        </div>
                        <span class="badge ${statusClass}">${statusText}</span>
                    </div>
                    <div class="appointment-details">
                        <p><strong>Fecha y hora:</strong> ${formattedDate}</p>
                        <p><strong>Programa:</strong> ${getProgramName(appointment.programId)}</p>
                        ${appointment.details ? `<p><strong>Detalles:</strong> ${appointment.details}</p>` : ''}
                    </div>
                    ${appointment.status === 'SCHEDULED' ? `
                        <div class="mt-2">
                            <button class="btn btn-sm btn-outline-danger cancel-appointment" data-id="${appointment.id}">
                                <i class="fas fa-times-circle me-1"></i> Cancelar Cita
                            </button>
                        </div>
                    ` : ''}
                </div>
            `;

            appointmentsList.innerHTML += appointmentHtml;
        });

        // Add event listeners for cancel buttons
        document.querySelectorAll('.cancel-appointment').forEach(button => {
            button.addEventListener('click', async (e) => {
                const appointmentId = e.target.getAttribute('data-id');
                if (confirm('¿Está seguro de que desea cancelar esta cita?')) {
                    try {
                        showLoadingState(true);
                        await API.cancelAppointment(appointmentId);
                        showNotification('Cita cancelada con éxito.', 'success');
                        // Refresh the appointments list
                        handleSearchByDocument();
                    } catch (error) {
                        showNotification(error.message, 'error');
                        showLoadingState(false);
                    }
                }
            });
        });
    }

    /**
     * Get appointment type name by ID
     * @param {string} id - Appointment type ID
     * @returns {string} - Appointment type name
     */
    function getAppointmentTypeName(id) {
        const type = appointmentTypes.find(type => type.id === id);
        return type ? type.name : 'Tipo desconocido';
    }

    /**
     * Get program name by ID
     * @param {string} id - Program ID
     * @returns {string} - Program name
     */
    function getProgramName(id) {
        const program = programs.find(program => program.id === id);
        return program ? program.name : 'Programa desconocido';
    }

    /**
     * Validate all form fields
     * @param {HTMLFormElement} form - The form element
     * @returns {boolean} - True if all fields are valid
     */
    function validateForm(form) {
        let isValid = true;

        // Validate each field with data-validation attribute
        form.querySelectorAll('[data-validation]').forEach(field => {
            if (!validateField(field)) {
                isValid = false;
            }
        });

        return isValid;
    }

    /**
     * Validate a single form field
     * @param {HTMLElement} field - The field to validate
     * @returns {boolean} - True if field is valid
     */
    function validateField(field) {
        const validationAttr = field.getAttribute('data-validation');
        // Si no hay atributo de validación, el campo es válido
        if (!validationAttr) return true;

        const validations = validationAttr.split('|');
        const value = field.value.trim();
        let isValid = true;
        let errorMessage = '';

        // Check each validation rule
        for (const validation of validations) {
            const [rule, param] = validation.split(':');

            switch (rule) {
                case 'required':
                    if (!value) {
                        isValid = false;
                        errorMessage = 'Este campo es requerido.';
                    }
                    break;

                case 'email':
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (value && !emailRegex.test(value)) {
                        isValid = false;
                        errorMessage = 'Por favor ingrese un correo electrónico válido.';
                    }
                    break;

                case 'minLength':
                    if (value && value.length < parseInt(param)) {
                        isValid = false;
                        errorMessage = `Este campo debe tener al menos ${param} caracteres.`;
                    }
                    break;

                case 'maxLength':
                    if (value && value.length > parseInt(param)) {
                        isValid = false;
                        errorMessage = `Este campo debe tener máximo ${param} caracteres.`;
                    }
                    break;

                case 'future':
                    if (value) {
                        const inputDate = new Date(value);
                        const now = new Date();
                        if (inputDate <= now) {
                            isValid = false;
                            errorMessage = 'La fecha debe ser futura.';
                        }
                    }
                    break;

                case 'max':
                    if (value && value.length > parseInt(param)) {
                        isValid = false;
                        errorMessage = `Este campo debe tener máximo ${param} caracteres.`;
                    }
                    break;
            }

            // Stop checking if already invalid
            if (!isValid) break;
        }

        // Update field styling and feedback
        const feedbackElement = document.getElementById(`${field.id}-feedback`);

        if (isValid) {
            field.classList.remove('is-invalid');

            // Solo añadimos la clase is-valid si el campo tiene valor
            if (value) {
                field.classList.add('is-valid');
            } else {
                field.classList.remove('is-valid');
            }

            // Ocultar mensaje de error
            if (feedbackElement) {
                feedbackElement.style.display = 'none';
            }
        } else {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');

            // Mostrar mensaje de error
            if (feedbackElement) {
                feedbackElement.textContent = errorMessage;
                feedbackElement.style.display = 'block';
            }
        }

        return isValid;
    }

    /**
     * Show loading state
     * @param {boolean} isLoading - Whether to show or hide loading state
     */
    function showLoadingState(isLoading) {
        const submitButton = document.getElementById('submit-btn');
        const submitSpinner = document.getElementById('submit-spinner');

        if (isLoading) {
            submitButton.disabled = true;
            submitSpinner.classList.remove('d-none');
        } else {
            submitButton.disabled = false;
            submitSpinner.classList.add('d-none');
        }
    }

    /**
     * Show notification toast
     * @param {string} message - The message to display
     * @param {string} type - The notification type (success or error)
     */
    function showNotification(message, type = 'success') {
        const toastElement = document.getElementById('notification-toast');
        const toastTitle = document.getElementById('toast-title');
        const toastMessage = document.getElementById('toast-message');

        // Remove previous classes
        toastElement.classList.remove('success', 'error');

        // Set type and content
        toastElement.classList.add(type);
        toastTitle.textContent = type === 'success' ? 'Éxito' : 'Error';
        toastMessage.textContent = message;

        // Show toast
        toast.show();
    }
});
