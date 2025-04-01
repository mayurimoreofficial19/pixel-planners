import React, { useState, useEffect, useRef } from "react";
import styles from "./dashboard/EventForm.css";
import commonStyles from "../../styles/common.module.css";
import { eventApi, venueApi, vendorApi, clientApi } from "../../services/api";
import VenueForm from "../VenueForm/VenueForm";
import VendorForm from "../VendorForm/VendorForm";
import ClientForm from "../ClientForm/ClientForm";
import Modal from "../Modal/Modal";
import Select from "react-select";

const EventForm = ({ onSubmit, onCancel, calendarId, initialData = null }) => {
  const formRef = useRef(null);
  const [formData, setFormData] = useState({
    name: "",
    date: "",
    time: "",
    venue: "",
    vendors: [],
    client: "",
    notes: "",
    ...(initialData || {}),
  });

  const [venues, setVenues] = useState([]);
  const [vendors, setVendors] = useState([]);
  const [clients, setClients] = useState([]);

  const [showVenueForm, setShowVenueForm] = useState(false);
  const [showVendorForm, setShowVendorForm] = useState(false);
  const [showClientForm, setShowClientForm] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        formRef.current &&
        !formRef.current.contains(event.target) &&
        onCancel
      ) {
        onCancel();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [onCancel]);

  const fetchData = async () => {
    try {
      const [venuesRes, vendorsRes, clientsRes] = await Promise.all([
        venueApi.getAllVenues(),
        vendorApi.getAllVendors(),
        clientApi.getAllClients(),
      ]);
      setVenues(venuesRes.data);
      setVendors(vendorsRes.data);
      setClients(clientsRes.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleVendorChange = (selectedOptions) => {
    setFormData((prev) => ({
      ...prev,
      vendors: selectedOptions.map((option) => option.value),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const eventWithCalendar = {
        ...formData,
        calendar: { id: calendarId },
      };
      const response = await eventApi.createEvent(eventWithCalendar);
      onSubmit(response.data);
    } catch (error) {
      console.error("Error creating event:", error);
    }
  };

  const handleVenueSubmit = async (newVenue) => {
    setVenues((prev) => [...prev, newVenue]);
    setFormData((prev) => ({ ...prev, venue: newVenue.id }));
    setShowVenueForm(false);
  };

  const handleVendorSubmit = async (newVendor) => {
    setVendors((prev) => [...prev, newVendor]);
    setFormData((prev) => ({
      ...prev,
      vendors: [...prev.vendors, newVendor.id],
    }));
    setShowVendorForm(false);
  };

  const handleClientSubmit = async (newClient) => {
    setClients((prev) => [...prev, newClient]);
    setFormData((prev) => ({ ...prev, client: newClient.id }));
    setShowClientForm(false);
  };

  const vendorOptions = vendors.map((vendor) => ({
    value: vendor.id,
    label: `${vendor.name} - ${vendor.services?.join(", ")}`,
  }));

  const selectedVendors = vendorOptions.filter((option) =>
    formData.vendors.includes(option.value)
  );

  return (
    <div className={styles.formContainer} ref={formRef}>
      <h2>{initialData ? "Edit Event" : "Add New Event"}</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formGroup}>
          <label className={styles.label}>Event Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className={styles.input}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Date</label>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            className={styles.input}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Time</label>
          <input
            type="time"
            name="time"
            value={formData.time}
            onChange={handleChange}
            className={styles.input}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Venue</label>
          <div className={styles.selectContainer}>
            <select
              name="venue"
              value={formData.venue}
              onChange={handleChange}
              className={styles.select}
              required
            >
              <option value="">Select a venue</option>
              {venues.map((venue) => (
                <option key={venue.id} value={venue.id}>
                  {venue.name}
                </option>
              ))}
            </select>
            <button
              type="button"
              onClick={() => setShowVenueForm(true)}
              className={`${commonStyles.btn} ${commonStyles.secondary}`}
            >
              Add New Venue
            </button>
          </div>
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Vendors</label>
          <div className={styles.selectContainer}>
            <Select
              isMulti
              isSearchable={false}
              name="vendors"
              options={vendorOptions}
              value={selectedVendors}
              onChange={handleVendorChange}
              className={styles.reactSelect}
              placeholder="Select vendors..."
              classNamePrefix="select"
              menuIsOpen={true}
              hideSelectedOptions={false}
              closeMenuOnSelect={false}
              noOptionsMessage={() => null}
            />
            <button
              type="button"
              onClick={() => setShowVendorForm(true)}
              className={`${commonStyles.btn} ${commonStyles.secondary}`}
            >
              Add New Vendor
            </button>
          </div>
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Client</label>
          <div className={styles.selectContainer}>
            <select
              name="client"
              value={formData.client}
              onChange={handleChange}
              className={styles.select}
              required
            >
              <option value="">Select a client</option>
              {clients.map((client) => (
                <option key={client.id} value={client.id}>
                  {client.name}
                </option>
              ))}
            </select>
            <button
              type="button"
              onClick={() => setShowClientForm(true)}
              className={`${commonStyles.btn} ${commonStyles.secondary}`}
            >
              Add New Client
            </button>
          </div>
        </div>

        <div className={styles.formGroup}>
          <label className={styles.label}>Notes</label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            className={styles.textarea}
          />
        </div>

        <div className={styles.buttonGroup}>
          <button
            type="submit"
            className={`${commonStyles.btn} ${commonStyles.primary}`}
          >
            {initialData ? "Update Event" : "Create Event"}
          </button>
          {onCancel && (
            <button
              type="button"
              onClick={onCancel}
              className={`${commonStyles.btn} ${commonStyles.secondary}`}
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      {showVenueForm && (
        <Modal onClose={() => setShowVenueForm(false)}>
          <VenueForm
            onSubmit={handleVenueSubmit}
            onCancel={() => setShowVenueForm(false)}
          />
        </Modal>
      )}

      {showVendorForm && (
        <Modal onClose={() => setShowVendorForm(false)}>
          <VendorForm
            onSubmit={handleVendorSubmit}
            onCancel={() => setShowVendorForm(false)}
          />
        </Modal>
      )}

      {showClientForm && (
        <Modal onClose={() => setShowClientForm(false)}>
          <ClientForm
            onSubmit={handleClientSubmit}
            onCancel={() => setShowClientForm(false)}
          />
        </Modal>
      )}
    </div>
  );
};

export default EventForm;
