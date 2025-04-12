import React from "react";
import "../../styles/components.css";

const SkillSearchResults = ({ skills, onEdit, onDelete }) => {
  if (skills.length === 0) {
    return (
      <div className="empty-state">
        <p>No skills found. Try adjusting your search or add a new skill!</p>
      </div>
    );
  }

  return (
    <div className="grid-container">
      {skills.map((skill) => (
        <div key={skill.id} className="skill-card">
          <div className="skill-header">
            <h3 className="skill-title">{skill.name}</h3>
            <div className="flex" style={{ gap: "0.5rem" }}>
              <button
                className="button button-outline"
                onClick={() => onEdit(skill)}
              >
                Edit
              </button>
              <button
                className="button button-secondary"
                onClick={() => onDelete(skill.id)}
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default SkillSearchResults;
