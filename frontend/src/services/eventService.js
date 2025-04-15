import axios from "axios";

const API_URL = "http://localhost:8080/api/events";
let WEATHER_API_KEY = "";
let WEATHER_API_URL = "";

// Axios instance
const publicAxios = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Fetch the weather API config from the backend
const fetchWeatherConfig = async () => {
  try {
    const response = await axios.get("http://localhost:8080/config/weather-config");
    WEATHER_API_KEY = response.data.weatherApiKey;
    WEATHER_BASE_URL = response.data.weatherApiUrl;
    console.log("Weather API Key:", WEATHER_API_KEY);
    console.log("Weather API URL:", WEATHER_API_URL);
  } catch (error) {
    console.error("Error fetching weather config:", error);
  }
};

export const getUpcomingEvents = async () => {
  await fetchWeatherConfig();

  try {
    const response = await publicAxios.get(`${API_URL}/upcoming-events`);
    const events = response.data;

    const enhancedEvents = await Promise.all(
      events.map(async (event) => {
        try {
          const weatherRes = await axios.get(WEATHER_API_URL, {
            params: {
              q: event.location,
              appid: WEATHER_API_KEY,
              units: "metric",
            },
          });

          const weather = weatherRes.data;

          return {
            ...event,
            temperature: `${weather.main.temp}°C`,
            weatherDescription: weather.weather[0].description,
            weatherIcon: weather.weather[0].icon,
          };
        } catch (weatherError) {
          console.warn(`Weather fetch failed for ${event.location}:`, weatherError);
          return {
            ...event,
            temperature: "N/A",
            weatherDescription: "Weather unavailable",
            weatherIcon: "01d", // fallback to sunny
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