import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { vendorApi } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import "../../styles/components.css";
import VendorForm from "./VendorForm";
import VendorSearch from "./VendorSearch";
import VendorSearchResults from "./VendorSearchResults";
import Modal from "../../components/common/Modal/Modal";

const VendorPage = () => {
  const [vendors, setVendors] = useState([]);
  const [selectedVendor, setSelectedVendor] = useState(null);
  const [showVendorForm, setShowVendorForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isAuthenticated, token, logout } = useAuth();
  const navigate = useNavigate();

  // Search state
  const [searchTerm, setSearchTerm] = useState("");
  const [searchType, setSearchType] = useState("name");
  const [searchResults, setSearchResults] = useState([]);

  useEffect(() => {
    if (!isAuthenticated || !token) {
      navigate("/login");
      return;
    }
    fetchVendors();
  }, [isAuthenticated, token, navigate]);

  const handleAuthError = async () => {
    await logout();
    navigate("/login");
  };

  const fetchVendors = async () => {
    try {
      setLoading(true);
      const response = await vendorApi.getAllVendors();
      setVendors(response.data || []);
      setSearchResults(response.data || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching vendors:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Unable to load vendors. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      setSearchResults(vendors);
      return;
    }

    try {
      let result;
      console.log("Searching for:", searchTerm, "by type:", searchType);

      switch (searchType) {
        case "name":
          result = await vendorApi.getVendorByName(searchTerm);
          console.log("Name search result:", result);
          break;
        case "location":
          result = await vendorApi.getVendorByLocation(searchTerm);
          console.log("Location search result:", result);
          break;
        case "skills":
                  result = await vendorApi.getVendorBySkill(searchTerm);
                  console.log("Skill search result:", result);
                  break;
        case "phone":
          result = await vendorApi.getVendorByPhoneNumber(searchTerm);
          console.log("Phone search result:", result);
          break;
        case "email":
          result = await vendorApi.getVendorByEmail(searchTerm);
          console.log("Email search result:", result);
          break;
        default:
          result = [];
      }

      // Extract data from response if it exists
      if (result && result.data) {
        result = result.data;
      }

      // Ensure result is an array
      const searchResults = Array.isArray(result) ? result : [result];
      console.log("Final search results:", searchResults);

      setSearchResults(searchResults.filter((item) => item !== null));
    } catch (err) {
      console.error("Error searching vendors:", err);
      setSearchResults([]);
    }
  };

  const handleAddVendor = () => {
    if (!token) {
      handleAuthError();
      return;
    }
    setSelectedVendor(null);
    setShowVendorForm(true);
  };

  const handleEditVendor = (vendor) => {
    if (!token) {
      handleAuthError();
      return;
    }
    setSelectedVendor(vendor);
    setShowVendorForm(true);
  };

  const handleDeleteVendor = async (vendorId) => {
    if (!token) {
      handleAuthError();
      return;
    }

    if (window.confirm("Are you sure you want to delete this vendor?")) {
      try {
        await vendorApi.deleteVendor(vendorId);
        setVendors(vendors.filter((vendor) => vendor.id !== vendorId));
        setSearchResults(searchResults.filter((vendor) => vendor.id !== vendorId));
        setError(null);
      } catch (err) {
        console.error("Error deleting vendor:", err);
        if (err.response?.status === 401 || err.response?.status === 403) {
          await handleAuthError();
        } else {
          setError("Failed to delete vendor. Please try again later.");
        }
      }
    }
  };

  const handleVendorSubmit = async (vendorData) => {
    if (!token) {
      handleAuthError();
      return;
    }

    try {
      if (selectedVendor) {
        // Update existing vendor
        const response = await vendorApi.updateVendor(
          selectedVendor.id,
          vendorData
        );
        setVendors(
          vendors.map((vendor) =>
            vendor.id === selectedVendor.id ? response.data : vendor
          )
        );
        setSearchResults(
          searchResults.map((vendor) =>
            vendor.id === selectedVendor.id ? response.data : vendor
          )
        );
      } else {
        // Create new vendor
        const response = await vendorApi.createVendor(vendorData);
        setVendors([...vendors, response.data]);
        setSearchResults([...searchResults, response.data]);
      }
      setShowVendorForm(false);
      setSelectedVendor(null);
      setError(null);
    } catch (err) {
      console.error("Error saving vendor:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Failed to save vendor. Please try again.");
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
        <h2 className="dashboard-title">Vendors</h2>
        <p className="dashboard-subtitle">Manage your event vendors</p>

        <VendorSearch
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          searchType={searchType}
          setSearchType={setSearchType}
          onSearch={handleSearch}
        />

        <button
          className="button button-primary"
          onClick={handleAddVendor}
          style={{ marginTop: "1rem" }}
        >
          Add New Vendor
        </button>
      </div>

      {error && (
        <div className="error-message" style={{ marginBottom: "1rem" }}>
          {error}
        </div>
      )}

      <VendorSearchResults
        vendors={searchResults}
        onEdit={handleEditVendor}
        onDelete={handleDeleteVendor}
      />

      {showVendorForm && (
        <Modal onClose={() => setShowVendorForm(false)}>
          <VendorForm
            initialData={selectedVendor}
            onSubmit={handleVendorSubmit}
//             skills={skills}
            onCancel={() => setShowVendorForm(false)}
          />
        </Modal>
      )}
    </div>
  );
};

export default VendorPage;