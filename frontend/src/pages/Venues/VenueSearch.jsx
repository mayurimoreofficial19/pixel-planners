import React from "react";
import "../../styles/components.css";

const VenueSearch = ({
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
      <select
        value={searchType}
        onChange={(e) => setSearchType(e.target.value)}
        className="search-select"
      >

        <option value="name">Name</option>
        <option value="location">Location</option>
        <option value="phone">Phone</option>
        <option value="email">Email</option>
      </select>
      <input
        type="text"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyPress} // Trigger search on Enter key press
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
