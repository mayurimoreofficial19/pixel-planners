import axios from "axios";

const API_URL = "http://localhost:8080/api/events";

const publicAxios = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export const getUpcomingEvents = async () => {
  try {
    const response = await publicAxios.get(`${API_URL}/upcoming-events`);
    const events = response.data;

    const enhancedEvents = await Promise.all(
      events.map(async (event) => {
        if (!event.venue || !event.venue.location) {
          return {
            ...event,
            weatherIcon: null,
            weatherDescription: "No location available",
            temperature: "N/A",
          };
        }

        try {
          const token = localStorage.getItem("token");
          if (!token) {
            throw new Error("No authentication token found");
          }

          const weatherRes = await axios.get(
            "http://localhost:8080/api/weather",
            {
              params: {
                location: event.venue.location,
                date: event.date,
              },
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (weatherRes.data.available === false) {
            return {
              ...event,
              weatherIcon: null,
              weatherDescription: "Weather data not available",
              temperature: "N/A",
            };
          }

          return {
            ...event,
            temperature: `${weatherRes.data.temperature}°F`,
            weatherDescription: weatherRes.data.description,
            weatherIcon: weatherRes.data.icon,
          };
        } catch (weatherError) {
          console.warn(
            `Weather fetch failed for ${event.venue.location}:`,
            weatherError
          );
          return {
            ...event,
            weatherIcon: null,
            weatherDescription: "Weather data not available",
            temperature: "N/A",
          };
        }
      })
    );

    return enhancedEvents;
  } catch (error) {
    console.error("Error fetching upcoming events:", error);
    throw error;
  }
};
