import React from 'react';
import { Link } from 'react-router-dom';
import Header from './Header';

const WelcomePage = () => {
  return (
    <div>
    <Header />
      <h1>WELCOME</h1>
      <p>The portal will allow you to create and manage events.</p>
      <img className="full-page-image" src="/wedding.jpg" alt="Welcome" />

    </div>
  );
};

export default WelcomePage;