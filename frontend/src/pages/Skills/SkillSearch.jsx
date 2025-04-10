import React from "react";
import "../../styles/components.css";

const SkillSearch = ({
  searchTerm,
  setSearchTerm,
  searchType,
  setSearchType,
  onSearch,
}) => {
  return (
    <div className="search-container" style={{ marginBottom: "1rem" }}>
      <<select
        value={searchType}
        onChange={(e) => setSearchType(e.target.value)}
        className="search-select"
      >

        <option value="name">Name</option>
      </select>>
      <input
        type="text"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder={`Search by ${searchType}...`}
        className="search-input"
      />
      <button onClick={onSearch} className="button button-primary">
        Search
      </button>
    </div>
  );
};

export default VenueSearch;
