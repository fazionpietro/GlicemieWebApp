import axios from 'axios';
import type { AuthContextType } from '../app/type/DataType';

// Configura axios per includere sempre i cookie
axios.defaults.withCredentials = true;
axios.defaults.baseURL = import.meta.env.VITE_API_KEY;

/**
 * Sets up Axios interceptors to handle authentication errors.
 * Redirects to login page on 401 Unauthorized responses.
 */
export const setupAxiosInterceptors = (auth: AuthContextType) => {
  // Request interceptor
  axios.interceptors.request.use(
    (config) => {
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Response interceptor per gestire errori di autenticazione
  axios.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (error.response?.status === 401) {
        // Token scaduto o non valido
        auth.logout();
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }
  );
};
