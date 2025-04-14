import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { skillApi } from "../../services/api";
import { useAuth } from "../../context/AuthContext";
import "../../styles/components.css";
import SkillForm from "./SkillForm";
import SkillSearch from "./SkillSearch";
import SkillSearchResults from "./SkillSearchResults";
import Modal from "../../components/common/Modal/Modal";

const SkillPage = () => {
    const [skills, setSkills] = useState([]);
    const [selectedSkill, setSelectedSkill] = useState(null);
    const [showSkillForm, setShowSkillForm] = useState(false);
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
    fetchSkills();
    }, [isAuthenticated, token, navigate]);

    const handleAuthError = async () => {
      await logout();
      navigate("/login");
    };

    const fetchSkills = async () => {
    try {
      setLoading(true);
      const response = await skillApi.getAllSkills();
      setSkills(response.data || []);
      setSearchResults(response.data || []);
      setError(null);
    } catch (err) {
      console.error("Error fetching skills:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Unable to load skills. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
    };

    const handleSearch = async () => {
        if (!searchTerm.trim()) {
          setSearchResults(skills);
          return;
        }

        try {
          let result;
          console.log("Searching for:", searchTerm, "by type:", searchType);

          switch (searchType) {
            case "name":
              result = await skillApi.getSkillByName(searchTerm);
              console.log("Name search result:", result);
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
          console.error("Error searching skills:", err);
          setSearchResults([]);
        }
    };

    const handleAddSkill = () => {
        if (!token) {
          handleAuthError();
          return;
        }
        setSelectedSkill(null);
        setShowSkillForm(true);
    };

    const handleEditSkill = (skill) => {
        if (!token) {
          handleAuthError();
          return;
        }
        setSelectedSkill(skill);
        setShowSkillForm(true);
    };

    const handleDeleteSkill = async (skillId) => {
        if (!token) {
          handleAuthError();
          return;
        }

        if (window.confirm("Are you sure you want to delete this skill?")) {
          try {
            await skillApi.deleteSkill(skillId);
            setSkills(skills.filter((skill) => skill.id !== skillId));
            setSearchResults(searchResults.filter((skill) => skill.id !== skillId));
            setError(null);
          } catch (err) {
            console.error("Error deleting skill:", err);
            if (err.response?.status === 401 || err.response?.status === 403) {
              await handleAuthError();
            } else {
              setError("Failed to delete skill. Please try again later.");
            }
          }
        }
    };

    const handleSkillSubmit = async (skillData) => {
        if (!token) {
          handleAuthError();
          return;
        }

    try {
        if (selectedSkill) {
        // Update existing skill
        const response = await skillApi.updateSkill(
          selectedSkill.id,
          skillData
        );
        setSkills(
          skills.map((skill) =>
            skill.id === selectedSkill.id ? response.data : skill
          )
        );
        setSearchResults(
          searchResults.map((skill) =>
            skill.id === selectedSkill.id ? response.data : skill
          )
    );
      } else {
        // Create new skill
        const response = await skillApi.createSkill(skillData);
        setSkills([...skills, response.data]);
        setSearchResults([...searchResults, response.data]);
      }
      setShowSkillForm(false);
      setSelectedSkill(null);
      setError(null);
    } catch (err) {
      console.error("Error saving skill:", err);
      if (err.response?.status === 401 || err.response?.status === 403) {
        await handleAuthError();
      } else {
        setError("Failed to save skill. Please try again.");
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
            <h2 className="dashboard-title">Skills</h2>
            <p className="dashboard-subtitle">Manage your event skills</p>

            <SkillSearch
              searchTerm={searchTerm}
              setSearchTerm={setSearchTerm}
              searchType={searchType}
              setSearchType={setSearchType}
              onSearch={handleSearch}
            />

            <button
              className="button button-primary"
              onClick={handleAddSkill}
              style={{ marginTop: "1rem" }}
            >
              Add New Skill
            </button>
          </div>

          {error && (
            <div className="error-message" style={{ marginBottom: "1rem" }}>
              {error}
            </div>
          )}

          <SkillSearchResults
            skills={searchResults}
            onEdit={handleEditSkill}
            onDelete={handleDeleteSkill}
          />

          {showSkillForm && (
            <Modal onClose={() => setShowSkillForm(false)}>
              <SkillForm
                initialData={selectedSkill}
                onSubmit={handleSkillSubmit}
                onCancel={() => setShowSkillForm(false)}
              />
            </Modal>
          )}
        </div>
    );
};

export default SkillPage;