import React, { useState, useEffect } from "react";
import { eventApi, venueApi, skillApi, vendorApi } from "../../services/api";
import "../../styles/components.css";

const EventForm = ({ onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    name: "",
    date: "",
    time: "",
    venue: null,
    vendor: null,
    notes: "",
  });

  const [venues, setVenues] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    const fetchVenues = async () => {
      try {
        const response = await venueApi.getAllVenues();
        if (response.data) {
          setVenues(response.data);
        }
      } catch (error) {
        console.error("Error fetching venues:", error);
        setErrors((prev) => ({
          ...prev,
          fetch: "Failed to load venues. Please try again.",
        }));
      } finally {
        setLoading(false);
      }
    };

    fetchVenues();
  }, []);

    useEffect(() => {
      const fetchVendors = async () => {
        try {
          const response = await vendorApi.getAllVendors();
          if (response.data) {
            setVendors(response.data);
          }
        } catch (error) {
          console.error("Error fetching vendors:", error);
          setErrors((prev) => ({
            ...prev,
            fetch: "Failed to load vendors. Please try again.",
          }));
        } finally {
          setLoading(false);
        }
      };

      fetchVendors();
    }, []);

  const validateForm = () => {
    const newErrors = {};
    if (!formData.name || formData.name.length < 3) {
      newErrors.name = "Event name must be at least 3 characters long";
    }
    if (!formData.date) {
      newErrors.date = "Please select a date";
    }
    if (!formData.time) {
      newErrors.time = "Please select a time";
    }
    if (!formData.venue) {
      newErrors.venue = "Please select a venue";
    }
    if (!formData.vendor) {
      newErrors.vendor = "Please select a vendor";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: undefined }));
    }
  };

  const handleVenueChange = (e) => {
    const selectedVenue = venues.find((v) => v.id === parseInt(e.target.value));
    setFormData((prev) => ({ ...prev, venue: selectedVenue }));
    if (errors.venue) {
      setErrors((prev) => ({ ...prev, venue: undefined }));
    }
  };

    const handleVendorChange = (e) => {
      const selectedVendor = vendors.find((v) => v.id === parseInt(e.target.value));
      setFormData((prev) => ({ ...prev, vendor: selectedVendor }));
      if (errors.vendor) {
        setErrors((prev) => ({ ...prev, vendor: undefined }));
      }
    };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      try {
        // Create a new object with just the venue ID
        const eventData = {
          name: formData.name,
          date: formData.date,
          time: formData.time,
          notes: formData.notes,
          venue: formData.venue ? { id: formData.venue.id } : null,
          vendor: formData.vendor ? { id: formData.vendor.id } : null,
        };
        await eventApi.createEvent(eventData);
        onSubmit();
      } catch (error) {
        setErrors((prev) => ({
          ...prev,
          submit: "Failed to create event. Please try again.",
        }));
      }
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  return (
    <div className="form-container">
      <div className="form-header">
        <h2 className="form-title">Add New Event</h2>
        <p className="form-subtitle">Enter the event details below</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Event Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className={`form-input ${errors.name ? "error" : ""}`}
            required
          />
          {errors.name && <div className="error-text">{errors.name}</div>}
        </div>

        <div className="form-group">
          <label className="form-label">Date</label>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            className={`form-input ${errors.date ? "error" : ""}`}
            required
          />
          {errors.date && <div className="error-text">{errors.date}</div>}
        </div>

        <div className="form-group">
          <label className="form-label">Time</label>
          <input
            type="time"
            name="time"
            value={formData.time}
            onChange={handleChange}
            className={`form-input ${errors.time ? "error" : ""}`}
            required
          />
          {errors.time && <div className="error-text">{errors.time}</div>}
        </div>

        <div className="form-group">
          <label className="form-label">Venue</label>
          <select
            name="venue"
            value={formData.venue?.id || ""}
            onChange={handleVenueChange}
            className={`form-input ${errors.venue ? "error" : ""}`}
            required
          >
            <option value="">Select a venue</option>
            {venues.map((venue) => (
              <option key={venue.id} value={venue.id}>
                {venue.name}
              </option>
            ))}
          </select>
          {errors.venue && <div className="error-text">{errors.venue}</div>}
        </div>



                <div className="form-group">
                  <label className="form-label">Vendor</label>
                  <select
                    name="vendor"
                    value={formData.vendor?.id || ""}
                    onChange={handleVendorChange}
                    className={`form-input ${errors.vendor ? "error" : ""}`}
                    required
                  >
                    <option value="">Select a vendor</option>
                    {vendors.map((vendor) => (
                      <option key={vendor.id} value={vendor.id}>
                        {vendor.name}
                      </option>
                    ))}
                  </select>
                  {errors.vendor && <div className="error-text">{errors.vendor}</div>}
                </div>






        <div className="form-group">
          <label className="form-label">Notes</label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            className="form-input"
            rows="4"
          />
        </div>

        {errors.submit && <div className="error-message">{errors.submit}</div>}

        <div className="flex" style={{ gap: "1rem", marginTop: "2rem" }}>
          <button type="submit" className="button button-primary">
            Create Event
          </button>
          <button
            type="button"
            onClick={onCancel}
            className="button button-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default EventForm;
