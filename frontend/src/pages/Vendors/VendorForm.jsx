import React, { useState, useEffect } from "react";
import Select from "react-select";
import { vendorApi, skillApi } from "../../services/api";
import "../../styles/components.css";
import styles from "./VendorForm.module.css";


const VendorForm = ({ initialData, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    name: "",
    location: "",
    emailAddress: "",
    phoneNumber: {
      phoneNumber: "",
      isValid: false,
    },
    skills: new Set(initialData?.skills || []),
    notes: "",
    ...(initialData
      ? {
          ...initialData,
          phoneNumber:
            typeof initialData.phoneNumber === "string"
              ? { phoneNumber: initialData.phoneNumber, isValid: true }
              : initialData.phoneNumber,
        }
      : {}),
  });

  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({});

  const validatePhoneNumber = (phoneNumber) => {
    // Match the backend validation pattern
    const pattern = /^(\(\d{3}\)\s?|\d{3}[-.]?)\d{3}[-.]?\d{4}$/;
    return pattern.test(phoneNumber);
  };

  useEffect(() => {
    const fetchSkills = async () => {
      try {
        const response = await skillApi.getAllSkills();
        if (response.data) {
          setSkills(response.data);
        }
      } catch (error) {
        console.error("Error fetching skills:", error);
        setErrors((prev) => ({
          ...prev,
          fetch: "Failed to load skills. Please try again.",
        }));
      } finally {
        setLoading(false);
      }
    };
    fetchSkills();
  }, []);

    const skillOptions = skills.map((skill) => ({
      value: skill.id,
      label: skill.name,
    }));

    const selectedSkillOptions = Array.from(formData.skills).map((skill) => ({
      value: skill.id,
      label: skill.name,
    }));

    const handleSkillChange = (selectedOptions) => {
      const selectedSkills = new Set(
        selectedOptions.map((option) =>
          skills.find((skill) => skill.id === option.value)
        )
      );

      setFormData((prev) => ({
        ...prev,
        skills: selectedSkills,
      }));

      if (errors.skills) {
        setErrors((prev) => ({ ...prev, skills: undefined }));
      }
    };



  const validateForm = () => {
    const newErrors = {};
    if (!formData.name || formData.name.length < 3) {
      newErrors.name = "Name must be at least 3 characters long";
    }
    if (!formData.location || formData.location.length < 3) {
      newErrors.location = "Location must be at least 3 characters long";
    }
    if (formData.skills.size === 0) {
        newErrors.skills = "Please select at least one skill";
    }
    if (
      !formData.emailAddress ||
      !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.emailAddress)
    ) {
      newErrors.emailAddress = "Please enter a valid email address";
    }
    if (
      !formData.phoneNumber.phoneNumber ||
      !validatePhoneNumber(formData.phoneNumber.phoneNumber)
    ) {
      newErrors.phoneNumber =
        "Please enter a valid phone number (e.g., (123) 456-7890 or 123-456-7890)";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "phoneNumber") {
      setFormData((prev) => ({
        ...prev,
        phoneNumber: {
          phoneNumber: value,
          isValid: validatePhoneNumber(value),
        },
      }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
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
        const vendorData = {
          name: formData.name,
          location: formData.location,
          emailAddress: formData.emailAddress,
          phoneNumber: formData.phoneNumber.phoneNumber,
          notes: formData.notes,
          skills: Array.from(formData.skills).map((skill) => ({ id: skill.id })),  // Convert Set to Array of objects
        };
        await vendorApi.createVendor(vendorData);
        onSubmit();
      } catch (error) {
        console.error("Error saving vendor:", error);
        setErrors((prev) => ({
          ...prev,
          submit: "Failed to save vendor. Please try again.",
        }));
      }
    }
  };


  return (
    <div className="form-container">
      <div className="form-header">
        <h2 className="form-title">
          {initialData ? "Edit Vendor" : "Add New Vendor"}
        </h2>
        <p className="form-subtitle">Enter the venue details below</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Vendor Name</label>
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
          <label className="form-label">Location</label>
          <input
            type="text"
            name="location"
            value={formData.location}
            onChange={handleChange}
            className={`form-input ${errors.location ? "error" : ""}`}
            required
          />
          {errors.location && (
            <div className="error-text">{errors.location}</div>
          )}
        </div>

        <div className="form-group">
          <label className="form-label">Email Address</label>
          <input
            type="email"
            name="emailAddress"
            value={formData.emailAddress}
            onChange={handleChange}
            className={`form-input ${errors.emailAddress ? "error" : ""}`}
            required
          />
          {errors.emailAddress && (
            <div className="error-text">{errors.emailAddress}</div>
          )}
        </div>

        <div className="form-group">
          <label className="form-label">Phone Number</label>
          <input
            type="tel"
            name="phoneNumber"
            value={formData.phoneNumber.phoneNumber}
            onChange={handleChange}
            className={`form-input ${errors.phoneNumber ? "error" : ""}`}
            placeholder="(123) 456-7890"
            required
          />
          {errors.phoneNumber && (
            <div className="error-text">{errors.phoneNumber}</div>
          )}
        </div>

        <div className="form-group">
          <label className="form-label">Skills</label>
          <Select
            isMulti
            name="skills"
            options={skillOptions}
            value={selectedSkillOptions}
            onChange={handleSkillChange}
            classNamePrefix="react-select"
          />
          {errors.skills && <div className="error-text">{errors.skills}</div>}
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
            {initialData ? "Update Vendor" : "Create Vendor"}
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

export default VendorForm;