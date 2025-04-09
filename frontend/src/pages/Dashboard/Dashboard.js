//import React, { useState, useEffect } from "react";
//import { useAuth } from "../../context/AuthContext";
//import Calendar from "./Calendar";
//
//const Dashboard = () => {
//  const { user, logout, token } = useAuth();
//  const [events, setEvents] = useState([]);
//  const [loading, setLoading] = useState(true);
//  const [error, setError] = useState(null);
//
//  useEffect(() => {
//    if (token) {
//      fetchEvents();
//    }
//  }, [token]); //Re-fetch when token changes
//
//  const fetchEvents = async () => {
//    try {
//      if (!token) {
//        throw new Error("No authentication token available");
//      }
//
//      const response = await fetch("http://localhost:8080/api/events", {
//        method: "GET",
//        headers: {
//          "Content-Type": "application/json",
//          Authorization: `Bearer ${token}`,
//        },
//      });
//
//      if (!response.ok) {
//        const errorData = await response.json().catch(() => null);
//        throw new Error(
//          errorData?.message || `Failed to fetch events: ${response.status}`
//        );
//      }
//
//      const data = await response.json();
//      setEvents(data);
//    } catch (err) {
//      console.error("Error fetching events:", err);
//      setError(err.message);
//    } finally {
//      setLoading(false);
//    }
//  };
//
//  const handleEventClick = (event) => {
//    //Handle event click - you can show details, edit form, etc.
//    console.log("Event clicked:", event);
//  };
//
//  if (loading)
//    return (
//      <div className="loading-container">
//        <div className="loading-spinner">Loading...</div>
//      </div>
//    );
//
//  if (error)
//    return (
//      <div className="error-container">
//        <div className="error-message">
//          <h3>Error Loading Events</h3>
//          <p>{error}</p>
//          <button onClick={fetchEvents} className="button button-primary">
//            Try Again
//          </button>
//        </div>
//      </div>
//    );
//
//  return (
//    <div className="dashboard-container">
//      <nav className="card">
//        <div className="container">
//          <div
//            className="flex"
//            style={{
//              justifyContent: "space-between",
//              alignItems: "center",
//              height: "4rem",
//            }}
//          >
//            <div className="flex" style={{ alignItems: "center" }}>
//              <h1 className="card-title">Event Vista</h1>
//            </div>
//            <div className="flex" style={{ alignItems: "center", gap: "1rem" }}>
//              <span className="card-content">
//                Welcome, {user?.name || "User"}
//              </span>
//              <button onClick={logout} className="button button-secondary">
//                Logout
//              </button>
//            </div>
//          </div>
//        </div>
//      </nav>
//
//      <main className="container" style={{ marginTop: "2rem" }}>
//        <div className="dashboard-header">
//          <h2 className="dashboard-title">Your Dashboard</h2>
//          <p className="dashboard-subtitle">Manage your events and venues</p>
//        </div>
//
//        <div className="dashboard-grid" style={{ gridTemplateColumns: "1fr" }}>
//          <div className="card">
//            <div className="card-header">
//              <h3 className="card-title">Calendar</h3>
//            </div>
//            <div className="card-content">
//              <Calendar events={events} onEventClick={handleEventClick} />
//            </div>
//          </div>
//
//          <div className="flex" style={{ gap: "1rem", marginTop: "2rem" }}>
//            <div className="card" style={{ flex: 1 }}>
//              <div className="card-header">
//                <h3 className="card-title">Upcoming Events</h3>
//              </div>
//              <div className="card-content">
//                {events.length > 0 ? (
//                  <ul className="event-list">
//                    {events
//                      .filter((event) => new Date(event.date) >= new Date())
//                      .slice(0, 5)
//                      .map((event) => (
//                        <li key={event.id} className="event-list-item">
//                          <div className="event-name">{event.name}</div>
//                          <div className="event-date">
//                            {new Date(event.date).toLocaleDateString()} at{" "}
//                            {event.time}
//                          </div>
//                        </li>
//                      ))}
//                  </ul>
//                ) : (
//                  <p>No upcoming events</p>
//                )}
//              </div>
//            </div>
//
//            <div className="card" style={{ flex: 1 }}>
//              <div className="card-header">
//                <h3 className="card-title">Quick Actions</h3>
//              </div>
//              <div className="card-content">
//                <div
//                  className="flex"
//                  style={{ gap: "1rem", marginTop: "1rem" }}
//                >
//                  <button className="button button-primary">Add Event</button>
//                  <button className="button button-outline">Add Venue</button>
//                </div>
//              </div>
//            </div>
//          </div>
//        </div>
//      </main>
//    </div>
//  );
//};
//
//export default Dashboard;


//-------------------------
import React, { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import Calendar from "./Calendar";
import EventForm from "./EventForm";
import { eventApi } from "../../services/api";
import "../../styles/components.css";

const Dashboard = () => {
  const { user, logout, token } = useAuth();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showEventForm, setShowEventForm] = useState(false);
  const [successMessage, setSuccessMessage] = useState(null);

  useEffect(() => {
    if (token) {
      fetchEvents();
    }
  }, [token]);

  const fetchEvents = async () => {
    try {
      setLoading(true);
      const response = await eventApi.getAllEvents();
      setEvents(response.data);
    } catch (err) {
      console.error("Error fetching events:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleAddEvent = () => {
    setShowEventForm(true);
  };

  const handleSaveEvent = async () => {
    try {
      setError(null);
      setSuccessMessage(null);
      await fetchEvents();
      setSuccessMessage("Event created successfully!");
      setShowEventForm(false);
    } catch (err) {
      console.error("Error saving event:", err);
      setError("Failed to create event. Please try again.");
    }
  };

  const handleCancelEvent = () => {
    setShowEventForm(false);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h3>Error Loading Events</h3>
          <p>{error}</p>
          <button onClick={fetchEvents} className="button button-primary">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <nav className="card">
        <div className="container">
          <div
            className="flex"
            style={{
              justifyContent: "space-between",
              alignItems: "center",
              height: "4rem",
            }}
          >
            <div className="flex" style={{ alignItems: "center" }}>
              <h1 className="card-title">Event Vista</h1>
            </div>
            <div className="flex" style={{ alignItems: "center", gap: "1rem" }}>
              <span className="card-content">
                Welcome, {user?.name || "User"}
              </span>
              <button
                onClick={handleAddEvent}
                className="button button-primary"
              >
                Add Event
              </button>
              <button onClick={logout} className="button button-secondary">
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      <div className="dashboard-content">
        {successMessage && (
          <div className="success-message" style={{ margin: "1rem 0" }}>
            {successMessage}
          </div>
        )}
        {error && (
          <div className="error-message" style={{ margin: "1rem 0" }}>
            {error}
          </div>
        )}
        <Calendar events={events} />
      </div>

      {showEventForm && (
        <div className="modal-overlay">
          <div className="modal-container">
            <EventForm
              onSubmit={handleSaveEvent}
              onCancel={handleCancelEvent}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;

