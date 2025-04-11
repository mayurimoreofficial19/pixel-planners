import React, { useState, useEffect } from "react";
import { userApi, authApi } from "../../services/api";
//import { useNavigate } from "react-router-dom";
import "../User/UserProfile.css";

const UserProfile = () => {
  const [formData, setFormData] = useState({
    id: "",
    name: "",
    emailAddress: "",
    pictureUrl: "",
  });

  const [selectedFile, setSelectedFile] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);
   //const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);
    authApi
      .getCurrentUser()
      .then((response) => {
        setFormData({
          id: response.data.id,
          name: response.data.name,
          emailAddress: response.data.emailAddress,
          pictureUrl: response.data.pictureUrl || "",
        });
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching user data:", error);
        setErrorMessage("Failed to load user data.");
        setLoading(false);
      });
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };


  const handleUpdate = (e) => {
    e.preventDefault();
    setLoading(true);
    userApi.updateUser(formData)
      .then(() => {
        setSuccessMessage("Profile updated successfully!");
        setErrorMessage("");
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error updating profile:", error);
        setErrorMessage("Failed to update profile.");
        setSuccessMessage("");
        setLoading(false);
      });
  };

  const handleDelete = () => {
    if (window.confirm("Are you sure you want to delete your profile?")) {
      setLoading(true);
      userApi
        .deleteUser(formData.id)
        .then(() => {
          setSuccessMessage("Profile deleted successfully!");
          setErrorMessage("");
          setLoading(false);
          //navigate("/login"); // Redirect to login page
        })
        .catch((error) => {
          console.error("Error deleting profile:", error);
          setErrorMessage("Failed to delete profile.");
          setSuccessMessage("");
          setLoading(false);
        });
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="user-profile">
      <h1>User Profile</h1>
      {errorMessage && <div className="error-message">{errorMessage}</div>}
      {successMessage && <div className="success-message">{successMessage}</div>}
      <form onSubmit={handleUpdate}>
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="emailAddress">Email Address:</label>
          <input
            type="email"
            id="emailAddress"
            name="emailAddress"
            value={formData.emailAddress}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="picture">Profile Picture:</label>
          <input
            type="file"
            id="picture"
            name="picture"
            onChange={handleFileChange}
          />
        </div>
        <button type="submit" className="submit-button">
          Update Profile
        </button>
      </form>
      <button onClick={handleDelete} className="delete-button">
        Delete Profile
      </button>
    </div>
  );
};

export default UserProfile;