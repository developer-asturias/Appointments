/**
 * API Service
 * Manages all API calls for the appointment system
 */
const API = {
    baseUrl: '/api',

    /**
     * Performs a fetch request with common options
     * @param {string} url - The URL to fetch
     * @param {Object} options - The fetch options
     * @returns {Promise<any>} - The fetch response parsed as JSON
     */
    async request(url, options = {}) {
        try {
            const response = await fetch(`${this.baseUrl}${url}`, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                }
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({
                    message: 'Error en la solicitud al servidor'
                }));

                throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API Request Error:', error);
            throw error;
        }
    },

    /**
     * Fetches all appointment types from the API
     * @returns {Promise<Array>} - List of appointment types
     */
    async getAppointmentTypes() {
        try {
            return await this.request('/appointments/types');
        } catch (error) {
            console.error('Error fetching appointment types:', error);
            throw new Error('No pudimos cargar los tipos de citas. Por favor, inténtelo de nuevo.');
        }
    },

    /**
     * Fetches all academic programs from the API
     * @returns {Promise<Array>} - List of academic programs
     */
    async getPrograms() {
        try {
            return await this.request('/appointments/programs');
        } catch (error) {
            console.error('Error fetching programs:', error);
            throw new Error('No pudimos cargar los programas académicos. Por favor, inténtelo de nuevo.');
        }
    },

    /**
     * Creates a new appointment
     * @param {Object} formData - The appointment form data
     * @returns {Promise<Object>} - The created appointment
     */
    async createAppointment(formData) {
        try {
            return await this.request('/appointments/create', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
        } catch (error) {
            console.error('Error creating appointment:', error);
            throw new Error(error.message || 'No pudimos crear la cita. Por favor, inténtelo de nuevo.');
        }
    },

    /**
     * Searches for appointments by document number
     * @param {string} documentNumber - The document number to search
     * @returns {Promise<Array>} - List of appointments
     */
    async searchAppointmentsByDocument(documentNumber) {
        try {
            return await this.request(`/appointments/search?document=${documentNumber}`);
        } catch (error) {
            console.error('Error searching appointments:', error);
            throw new Error('No pudimos encontrar citas con ese documento. Por favor, inténtelo de nuevo.');
        }
    },

    /**
     * Cancels an appointment
     * @param {string} appointmentId - The appointment ID to cancel
     * @returns {Promise<Object>} - The cancelled appointment
     */
    async cancelAppointment(appointmentId) {
        try {
            return await this.request(`/appointments/cancel/${appointmentId}`, {
                method: 'PUT'
            });
        } catch (error) {
            console.error('Error cancelling appointment:', error);
            throw new Error('No pudimos cancelar la cita. Por favor, inténtelo de nuevo.');
        }
    }
};
