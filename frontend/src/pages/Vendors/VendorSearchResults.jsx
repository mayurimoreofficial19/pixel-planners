import React from "react";
import "../../styles/components.css";

const VendorSearchResults = ({ vendors, onEdit, onDelete }) => {
  if (vendors.length === 0) {
    return (
      <div className="empty-state">
        <p>No vendors found. Try adjusting your search or add a new vendor!</p>
      </div>
    );
  }

  return (
    <div className="grid-container">
      {vendors.map((vendor) => (
        <div key={vendor.id} className="vendor-card">
          <div className="vendor-header">
            <h3 className="vendor-title">{vendor.name}</h3>
            <div className="flex" style={{ gap: "0.5rem" }}>
              <button
                className="button button-outline"
                onClick={() => onEdit(vendor)}
              >
                Edit
              </button>
              <button
                className="button button-secondary"
                onClick={() => onDelete(vendor.id)}
              >
                Delete
              </button>
            </div>
          </div>
          <div className="vendor-details">
            <p>📍 {vendor.location}</p>
              <p>
                ⚙️ Skills:{" "}
                {Array.isArray(vendor.skills)
                  ? vendor.skills.map((s) => s.name).join(", ")
                  : "None"}
              </p>
            <p>📧 {vendor.emailAddress}</p>
            <p>
              📞{" "}
              {typeof vendor.phoneNumber === "object"
                ? vendor.phoneNumber.phoneNumber
                : vendor.phoneNumber}
            </p>
            {vendor.notes && <p>📝 {vendor.notes}</p>}
          </div>
        </div>
      ))}
    </div>
  );
};

export default VendorSearchResults;
