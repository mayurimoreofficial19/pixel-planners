import React from "react";
import "../../styles/components.css";

const VenueSearchResults = ({ venues, onEdit, onDelete }) => {
  if (venues.length === 0) {
    return (
      <div className="empty-state">
        <p>No venues found. Try adjusting your search or add a new venue!</p>
      </div>
    );
  }

  return (
    <div className="grid-container">
      {venues.map((venue) => (
        <div key={venue.id} className="venue-card">
          <div className="venue-header">
            <h3 className="venue-title">{venue.name}</h3>
            <div className="flex" style={{ gap: "0.5rem" }}>
              <button
                className="button button-outline"
                onClick={() => onEdit(venue)}
              >
                Edit
              </button>
              <button
                className="button button-secondary"
                onClick={() => onDelete(venue.id)}
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
      ))}
    </div>
  );
};

export default VenueSearchResults;
