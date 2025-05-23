/**
 * API para manejar las interacciones con el servidor
 */
const API = {
    /**
     * URL base para todas las peticiones
     */
    baseUrl: '/api',

    /**
     * Método para realizar peticiones HTTP
     * @param {string} method - Método HTTP (GET, POST, PUT, PATCH, DELETE)
     * @param {string} endpoint - Endpoint a consultar
     * @param {Object} data - Datos a enviar (opcional)
     * @returns {Promise<any>} - Respuesta del servidor
     */
    async fetchJson(method, endpoint, data = null) {
        try {
            const url = `${this.baseUrl}${endpoint}`;
            const options = {
                method,
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
                options.body = JSON.stringify(data);
            }

            const response = await fetch(url, options);

            // Si la respuesta no es exitosa, lanzar error
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
            }

            // Si la respuesta está vacía, devolver null
            if (response.status === 204) {
                return null;
            }

            // Devolver datos en formato JSON
            return await response.json();
        } catch (error) {
            console.error(`Error en la petición a ${endpoint}:`, error);
            throw error;
        }
    },

    /**
     * Obtener programas académicos
     * @returns {Promise<Array>} - Lista de programas
     */
    async getPrograms() {
        return this.fetchJson('GET', '/programs');
    },

    /**
     * Obtener tipos de citas
     * @returns {Promise<Array>} - Lista de tipos de citas
     */
    async getAppointmentTypes() {
        return this.fetchJson('GET', '/appointment-types');
    },

    /**
     * Obtener todas las citas
     * @returns {Promise<Array>} - Lista de citas
     */
    async getAppointments() {
        return this.fetchJson('GET', '/appointments');
    },

    /**
     * Obtener citas por correo electrónico
     * @param {string} email - Correo electrónico
     * @returns {Promise<Array>} - Lista de citas
     */
    async getAppointmentsByEmail(email) {
        return this.fetchJson('GET', `/appointments/email/${email}`);
    },

    /**
     * Obtener citas por número de documento (cédula)
     * @param {string} document - Número de documento
     * @returns {Promise<Array>} - Lista de citas
     */
    async getAppointmentsByDocument(document) {
        return this.fetchJson('GET', `/appointments/document/${document}`);
    },

    /**
     * Crear una nueva cita
     * @param {Object} appointmentData - Datos de la cita
     * @returns {Promise<Object>} - Cita creada
     */
    async createAppointment(appointmentData) {
        return this.fetchJson('POST', '/appointments', appointmentData);
    },

    /**
     * Actualizar el estado de una cita
     * @param {number} id - ID de la cita
     * @param {string} status - Nuevo estado
     * @returns {Promise<Object>} - Cita actualizada
     */
    async updateAppointmentStatus(id, status) {
        return this.fetchJson('PATCH', `/appointments/${id}/status`, { status });
    },

    /**
     * Actualizar el enlace de Teams de una cita
     * @param {number} id - ID de la cita
     * @param {string} teamsLink - Enlace de Teams
     * @returns {Promise<Object>} - Cita actualizada
     */
    async updateAppointmentTeamsLink(id, teamsLink) {
        return this.fetchJson('PATCH', `/appointments/${id}/teams-link`, { teamsLink });
    }
};