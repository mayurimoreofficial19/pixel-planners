import React, { useState, useEffect } from "react";
import { skillApi } from "../../services/api";
import "../../styles/components.css";
import styles from "./SkillForm.module.css";

const SkillForm = ({ initialData, onSubmit, onCancel }) => {
    const [formData, setFormData] = useState({
        name: "",
    });

    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        if (!formData.name || formData.name.length < 3) {
          newErrors.name = "Name must be at least 3 characters long";
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

    const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      try {
        // Transform the data to match the backend format
        const skillData = {
          ...formData,
        };
        console.log("Submitting skill data:", skillData);
        onSubmit(skillData);
      } catch (error) {
        console.error("Error saving skill:", error);
        setErrors((prev) => ({
          ...prev,
          submit: "Failed to save skill. Please try again.",
        }));
      }
    }
    };

    return (
        <div className="form-container">
          <div className="form-header">
            <h2 className="form-title">
              {initialData ? "Edit Skill" : "Add New Skill"}
            </h2>
            <p className="form-subtitle">Enter the skill details below</p>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Skill Name</label>
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

            {errors.submit && <div className="error-message">{errors.submit}</div>}

            <div className="flex" style={{ gap: "1rem", marginTop: "2rem" }}>
              <button type="submit" className="button button-primary">
                {initialData ? "Update Skill" : "Create Skill"}
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

export default SkillForm;
