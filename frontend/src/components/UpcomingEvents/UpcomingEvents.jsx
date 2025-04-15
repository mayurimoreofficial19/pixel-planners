import React, { useState, useEffect } from "react";
import axios from "axios";
import styles from "./UpcomingEvents.module.css";

const getWeatherEmoji = (icon) => {
  const emojiMap = {
    "01d": "☀️",
    "01n": "🌙",
    "02d": "⛅",
    "02n": "☁️",
    "03d": "☁️",
    "03n": "☁️",
    "04d": "☁️",
    "04n": "☁️",
    "09d": "🌧️",
    "09n": "🌧️",
    "10d": "🌦️",
    "10n": "🌧️",
    "11d": "⛈️",
    "11n": "⛈️",
    "13d": "❄️",
    "13n": "❄️",
    "50d": "🌫️",
    "50n": "🌫️",
  };
  return emojiMap[icon] || "☀️";
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString("en-US", { month: "long", day: "numeric" });
};

const UpcomingEvents = ({ events = [] }) => {
  const [upcomingEvents, setUpcomingEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (events.length > 0) {
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      const upcoming = events
        .filter((event) => {
          const eventDate = new Date(event.date);
          return eventDate >= today;
        })
        .sort((a, b) => new Date(a.date) - new Date(b.date));

      // Fetch weather data for each event
      const fetchWeatherData = async () => {
        setLoading(true);
        try {
          const token = localStorage.getItem("token");
          if (!token) {
            throw new Error("No authentication token found");
          }

          const eventsWithWeather = await Promise.all(
            upcoming.map(async (event) => {
              if (event.venue && event.venue.location) {
                try {
                  const response = await axios.get(
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
                  return {
                    ...event,
                    weatherIcon: response.data.icon,
                    weatherDescription: response.data.description,
                    temperature: response.data.temperature,
                  };
                } catch (weatherError) {
                  console.warn(
                    `Weather fetch failed for ${event.venue.location}:`,
                    weatherError
                  );
                  return event;
                }
              }
              return event;
            })
          );
          setUpcomingEvents(eventsWithWeather);
        } catch (err) {
          setError(
            "Failed to fetch weather data: " + (err.message || "Unknown error")
          );
          setUpcomingEvents(upcoming);
        } finally {
          setLoading(false);
        }
      };

      fetchWeatherData();
    }
  }, [events]);

  if (loading)
    return (
      <div className={`${styles.upcomingEvents} ${styles.loading}`}>
        Loading upcoming events...
      </div>
    );
  if (error)
    return (
      <div className={`${styles.upcomingEvents} ${styles.error}`}>{error}</div>
    );
  if (upcomingEvents.length === 0)
    return (
      <div className={`${styles.upcomingEvents} ${styles.empty}`}>
        No upcoming events
      </div>
    );

  return (
    <div className={styles.upcomingEvents}>
      <h2>Upcoming Events</h2>
      <div className={styles.eventsList}>
        {upcomingEvents.map((event) => (
          <div key={event.id} className={styles.eventCard}>
            <div className={styles.eventDate}>🗓️ {formatDate(event.date)}</div>
            <div className={styles.eventName}>{event.name}</div>
            <div className={styles.eventLocation}>
              {event.venue ? event.venue.name : "No venue set"}
            </div>
            {event.weatherIcon && (
              <div className={styles.eventWeather}>
                {getWeatherEmoji(event.weatherIcon)} {event.weatherDescription},{" "}
                {event.temperature}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default UpcomingEvents;
