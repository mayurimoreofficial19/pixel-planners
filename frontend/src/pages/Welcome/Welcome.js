import React from 'react';
import styles from '../Welcome/Welcome.module.css';// Import the CSS file
import { Link } from 'react-router-dom';

const Welcome = () => {
  return (
    <div className={styles['welcome-container']}>
    <div className={styles['welcome-content']}>
      <h1 className={styles['welcome-title']}>WELCOME to EVENTVISTA</h1>
      <p>The portal will allow you to create and manage events</p>
              <Link to="/login" className={styles['cta-button']}>
                Get Started
              </Link>

      </div>

    </div>
  );
};

export default Welcome;