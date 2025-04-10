import React from "react";
import "../../styles/components.css";

const SkillSearch = ({
  searchTerm,
  setSearchTerm,
  searchType,
  setSearchType,
  onSearch,
}) => {
  // Trigger search when the user presses Enter
    const handleKeyPress = (e) => {
      if (e.key === "Enter") {
        onSearch();
      }
    };

    return (
      <div className="search-container" style={{ marginBottom: "1rem" }}>
        <label htmlFor="searchTerm" className="form-label">
          Name
        </label>
        <input
          id="searchTerm"
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyDown={handleKeyPress} // Trigger search on Enter key press
          placeholder={`Search by name...`}
          className="search-input"
        />
        <button onClick={onSearch} className="button button-primary">
          Search
        </button>
      </div>
   );
};

  export default SkillSearch;
