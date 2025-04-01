import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { venueApi } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import VenueForm from "../venueform/VenueForm";
import Modal from "../Modal/Modal";
import "../../styles/components.css";

const VenuePage = () => {
  const [venues, setVenues] = useState([]);
  const [selectedVenue, setSelectedVenue] = useState(null);
  const [showVenueForm, setShowVenueForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isAuthenticated, token, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated || !token) {
      navigate("/login");
      return;
    }
    fetchVenues();
  }, [isAuthenticated, token, navigate]);

  const handleAuthError = async () => {
    await logout();
    navigate("/login");
  };

  const fetchVenues = async () => {
    try {
      setLoading(true);
      const response = await venueApi.getAllVenues();
      setVenues(response.data || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching venues:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Unable to load venues. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleAddVenue = () => {
    const currentToken = localStorage.getItem("token");
    console.log(
      "Current token when adding venue:",
      currentToken ? "Token exists" : "No token"
    );

    if (!token) {
      handleAuthError();
      return;
    }
    setSelectedVenue(null);
    setShowVenueForm(true);
  };

  const handleEditVenue = (venue) => {
    if (!token) {
      handleAuthError();
      return;
    }
    setSelectedVenue(venue);
    setShowVenueForm(true);
  };

  const handleDeleteVenue = async (venueId) => {
    if (!token) {
      handleAuthError();
      return;
    }

    if (window.confirm("Are you sure you want to delete this venue?")) {
      try {
        await venueApi.deleteVenue(venueId);
        setVenues(venues.filter((venue) => venue.id !== venueId));
        setError(null);
      } catch (err) {
        console.error("Error deleting venue:", err);
        if (err.response?.status === 401 || err.response?.status === 403) {
          await handleAuthError();
        } else {
          setError("Failed to delete venue. Please try again later.");
        }
      }
    }
  };

  const handleVenueSubmit = async (venueData) => {
    if (!token) {
      handleAuthError();
      return;
    }

    try {
      if (selectedVenue) {
        // Update existing venue
        const response = await venueApi.updateVenue(
          selectedVenue.id,
          venueData
        );
        setVenues(
          venues.map((venue) =>
            venue.id === selectedVenue.id ? response.data : venue
          )
        );
      } else {
        // Create new venue
        const response = await venueApi.createVenue(venueData);
        setVenues([...venues, response.data]);
      }
      setShowVenueForm(false);
      setSelectedVenue(null);
      setError(null);
    } catch (err) {
      console.error("Error saving venue:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Failed to save venue. Please try again.");
      }
    }
  };

  if (!isAuthenticated || !token) {
    return null; // Will redirect in useEffect
  }

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  return (
    <div className="container" style={{ padding: "2rem" }}>
      <div className="dashboard-header">
        <h2 className="dashboard-title">Venues</h2>
        <p className="dashboard-subtitle">Manage your event venues</p>
        <button
          className="button button-primary"
          onClick={handleAddVenue}
          style={{ marginTop: "1rem" }}
        >
          Add New Venue
        </button>
      </div>

      {error && (
        <div className="error-message" style={{ marginBottom: "1rem" }}>
          {error}
        </div>
      )}

      <div className="grid-container">
        {venues.length === 0 ? (
          <div className="empty-state">
            <p>
              No venues added yet. Click "Add New Venue" to create your first
              venue!
            </p>
          </div>
        ) : (
          venues.map((venue) => (
            <div key={venue.id} className="venue-card">
              <div className="venue-header">
                <h3 className="venue-title">{venue.name}</h3>
                <div className="flex" style={{ gap: "0.5rem" }}>
                  <button
                    className="button button-outline"
                    onClick={() => handleEditVenue(venue)}
                  >
                    Edit
                  </button>
                  <button
                    className="button button-secondary"
                    onClick={() => handleDeleteVenue(venue.id)}
                  >
                    Delete
                  </button>
                </div>
              </div>
              <div className="venue-details">
                <p>📍 {venue.location}</p>
                <p>👥 Capacity: {venue.capacity}</p>
                <p>📧 {venue.emailAddress}</p>
                <p>
                  📞{" "}
                  {typeof venue.phoneNumber === "object"
                    ? venue.phoneNumber.phoneNumber
                    : venue.phoneNumber}
                </p>
                {venue.notes && <p>📝 {venue.notes}</p>}
              </div>
            </div>
          ))
        )}
      </div>

      {showVenueForm && (
        <Modal onClose={() => setShowVenueForm(false)}>
          <VenueForm
            initialData={selectedVenue}
            onSubmit={handleVenueSubmit}
            onCancel={() => setShowVenueForm(false)}
          />
        </Modal>
      )}
    </div>
  );
};

export default VenuePage;
