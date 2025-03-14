import React from 'react';
import { Link } from 'react-router-dom';
//import './Header.css'; // Import the CSS file

const Header = () => {
  return (
    <header className="app-header">
            <div className="app-name">
              <Link to="/">Event Vista</Link>
            </div>
      <nav className="nav-links">
        <span class="bold">ADMIN PORTAL</span> | &nbsp;
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
      </nav>
    </header>
  );
};

export default Header;