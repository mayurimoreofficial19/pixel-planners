import axios from "axios";

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

// Configure axios defaults
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add a request interceptor to include the token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle authentication errors
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const { status, data } = error.response;
      console.log("API Response Headers:", error.response.headers);
      console.log("Request URL:", error.config.url);
      console.log("Request Headers:", error.config.headers);

      if (status === 401 || status === 403) {
        // Clear token on auth errors
        console.error(`Authentication error (${status}):`, data);
        localStorage.removeItem("token");
      }
    } else if (error.request) {
      console.error("Network error:", error.message);
    }
    return Promise.reject(error);
  }
);

export const venueApi = {
  getAllVenues: () => axiosInstance.get("/api/venues"),
  getVenue: (id) => axiosInstance.get(`/api/venues/${id}`),
  createVenue: (data) => axiosInstance.post("/api/venues", data),
  updateVenue: (id, data) => axiosInstance.put(`/api/venues/${id}`, data),
  deleteVenue: (id) => axiosInstance.delete(`/api/venues/${id}`),
  getVenueEvents: (id) => axiosInstance.get(`/api/events/venue/${id}`),
};

export const authApi = {
  login: (credentials) => axiosInstance.post("/api/auth/login", credentials),
  register: (userData) => axiosInstance.post("/api/auth/register", userData),
  getCurrentUser: () => axiosInstance.get("/api/auth/user"),
  logout: () => axiosInstance.post("/api/auth/logout"),
};
