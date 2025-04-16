import React, { useState, useEffect } from "react";
import { eventApi, venueApi, vendorApi } from "../../services/api";
import styles from "./EventActionsModal.module.css";

const EventActionsModal = ({ event, onClose, onEventUpdated }) => {
  const [action, setAction] = useState("view"); // 'view', 'edit', 'rebook'
  const [formData, setFormData] = useState({
    name: event.name,
    date: event.date,
    time: event.time,
    venue: event.venue,
    vendors: event.vendors || [],
    notes: event.notes || "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [venues, setVenues] = useState([]);
  const [vendors, setVendors] = useState([]);

  useEffect(() => {
    const fetchVenuesAndVendors = async () => {
      try {
        const [venuesResponse, vendorsResponse] = await Promise.all([
          venueApi.getAllVenues(),
          vendorApi.getAllVendors(),
        ]);
        setVenues(venuesResponse.data || []);
        setVendors(vendorsResponse.data || []);
      } catch (err) {
        console.error("Error fetching venues and vendors:", err);
      }
    };

    if (action === "rebook") {
      fetchVenuesAndVendors();
    }
  }, [action]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleVenueChange = (e) => {
    const venueId = e.target.value;
    setFormData((prev) => ({
      ...prev,
      venue: venueId ? { id: parseInt(venueId) } : null,
    }));
  };

  const handleVendorChange = (e) => {
    const vendorId = parseInt(e.target.value);
    if (vendorId && !formData.vendors.some((v) => v.id === vendorId)) {
      const selectedVendor = vendors.find((v) => v.id === vendorId);
      setFormData((prev) => ({
        ...prev,
        vendors: [...prev.vendors, { id: vendorId, name: selectedVendor.name }],
      }));
    }
  };

  const removeVendor = (vendorId) => {
    setFormData((prev) => ({
      ...prev,
      vendors: prev.vendors.filter((v) => v.id !== vendorId),
    }));
  };

  const handleEdit = async () => {
    setLoading(true);
    setError(null);
    try {
      await eventApi.updateEvent(event.id, formData);
      setSuccessMessage("Event updated successfully!");
      onEventUpdated();
      setAction("view");
    } catch (err) {
      setError("Failed to update event: " + (err.message || "Unknown error"));
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this event?")) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await eventApi.deleteEvent(event.id);
      setSuccessMessage("Event deleted successfully!");
      onEventUpdated();
      onClose();
    } catch (err) {
      setError("Failed to delete event: " + (err.message || "Unknown error"));
    } finally {
      setLoading(false);
    }
  };

  const handleRebook = async () => {
    setLoading(true);
    setError(null);
    try {
      const rebookData = {
        name: formData.name,
        date: formData.date,
        time: formData.time,
        notes: formData.notes || "",
        venue: formData.venue,
        vendors: formData.vendors.map((v) => ({ id: v.id })),
      };

      console.log("Rebooking with data:", rebookData);

      const response = await eventApi.rebookEvent(event.id, rebookData);
      console.log("Rebook response:", response);

      setSuccessMessage("Event rebooked successfully!");
      onEventUpdated();
      onClose();
    } catch (err) {
      console.error("Rebook error:", err);
      setError(
        "Failed to rebook event: " +
          (err.response?.data?.message || err.message || "Unknown error")
      );
    } finally {
      setLoading(false);
    }
  };

  const renderViewMode = () => (
    <div className={styles.viewMode}>
      <h2>{event.name}</h2>
      <div className={styles.eventDetails}>
        <p>
          <strong>Date:</strong> {new Date(event.date).toLocaleDateString()}
        </p>
        <p>
          <strong>Time:</strong> {event.time}
        </p>
        {event.venue && (
          <p>
            <strong>Venue:</strong> {event.venue.name}
          </p>
        )}
        {event.vendors && event.vendors.length > 0 && (
          <p>
            <strong>Vendors:</strong>{" "}
            {event.vendors.map((v) => v.name).join(", ")}
          </p>
        )}
        {event.notes && (
          <p>
            <strong>Notes:</strong> {event.notes}
          </p>
        )}
      </div>
      <div className={styles.actionButtons}>
        <button onClick={() => setAction("edit")} className={styles.button}>
          Edit
        </button>
        <button onClick={() => setAction("rebook")} className={styles.button}>
          Rebook
        </button>
        <button
          onClick={handleDelete}
          className={`${styles.button} ${styles.deleteButton}`}
        >
          Delete
        </button>
      </div>
    </div>
  );

  const renderEditMode = () => (
    <div className={styles.editMode}>
      <h2>Edit Event</h2>
      <div className={styles.formGroup}>
        <label>Event Name</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          className={styles.input}
        />
      </div>
      <div className={styles.formGroup}>
        <label>Date</label>
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleInputChange}
          className={styles.input}
        />
      </div>
      <div className={styles.formGroup}>
        <label>Time</label>
        <input
          type="time"
          name="time"
          value={formData.time}
          onChange={handleInputChange}
          className={styles.input}
        />
      </div>
      <div className={styles.formGroup}>
        <label>Venue</label>
        <select
          name="venue"
          value={formData.venue?.id || ""}
          onChange={handleVenueChange}
          className={styles.input}
        >
          <option value="">Select a venue</option>
          {/* Add venue options here */}
        </select>
      </div>
      <div className={styles.formGroup}>
        <label>Notes</label>
        <textarea
          name="notes"
          value={formData.notes}
          onChange={handleInputChange}
          className={styles.textarea}
        />
      </div>
      <div className={styles.actionButtons}>
        <button
          onClick={handleEdit}
          className={styles.button}
          disabled={loading}
        >
          {loading ? "Saving..." : "Save Changes"}
        </button>
        <button onClick={() => setAction("view")} className={styles.button}>
          Cancel
        </button>
      </div>
    </div>
  );

  const renderRebookMode = () => (
    <div className={styles.rebookMode}>
      <h2>Rebook Event</h2>
      <div className={styles.formGroup}>
        <label>Event Name</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          className={styles.input}
          placeholder="Enter new event name"
        />
      </div>
      <div className={styles.formGroup}>
        <label>New Date</label>
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleInputChange}
          className={styles.input}
          required
        />
      </div>
      <div className={styles.formGroup}>
        <label>New Time</label>
        <input
          type="time"
          name="time"
          value={formData.time}
          onChange={handleInputChange}
          className={styles.input}
          required
        />
      </div>
      <div className={styles.formGroup}>
        <label>Venue</label>
        <select
          name="venue"
          value={formData.venue?.id || ""}
          onChange={handleVenueChange}
          className={styles.input}
        >
          <option value="">Select a venue (optional)</option>
          {venues.map((venue) => (
            <option key={venue.id} value={venue.id}>
              {venue.name}
            </option>
          ))}
        </select>
      </div>
      <div className={styles.formGroup}>
        <label>Vendors</label>
        <select
          name="vendors"
          value=""
          onChange={handleVendorChange}
          className={styles.input}
        >
          <option value="">Select vendors (optional)</option>
          {vendors
            .filter(
              (vendor) => !formData.vendors.some((v) => v.id === vendor.id)
            )
            .map((vendor) => (
              <option key={vendor.id} value={vendor.id}>
                {vendor.name}
              </option>
            ))}
        </select>
        {formData.vendors.length > 0 && (
          <div className={styles.selectedVendors}>
            {formData.vendors.map((vendor) => (
              <div key={vendor.id} className={styles.selectedVendor}>
                <span>{vendor.name}</span>
                <button
                  type="button"
                  onClick={() => removeVendor(vendor.id)}
                  className={styles.removeVendor}
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
      <div className={styles.formGroup}>
        <label>Notes</label>
        <textarea
          name="notes"
          value={formData.notes}
          onChange={handleInputChange}
          className={styles.textarea}
          placeholder="Add any notes about the rebooked event"
        />
      </div>
      <div className={styles.actionButtons}>
        <button
          onClick={handleRebook}
          className={styles.button}
          disabled={loading}
        >
          {loading ? "Rebooking..." : "Rebook Event"}
        </button>
        <button onClick={() => setAction("view")} className={styles.button}>
          Cancel
        </button>
      </div>
    </div>
  );

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <button className={styles.closeButton} onClick={onClose}>
          ×
        </button>
        {error && <div className={styles.error}>{error}</div>}
        {successMessage && (
          <div className={styles.success}>{successMessage}</div>
        )}
        {action === "view" && renderViewMode()}
        {action === "edit" && renderEditMode()}
        {action === "rebook" && renderRebookMode()}
      </div>
    </div>
  );
};

export default EventActionsModal;
