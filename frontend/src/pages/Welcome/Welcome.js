import React from 'react';
import Header from '../Welcome/Header';
import styles from '../Welcome/Welcome.module.css'; // Import the CSS file

const Welcome = () => {
  return (
    <div className={styles['welcome-container']}>
    <Header />
    <div className={styles['welcome-content']}>
      <h1>WELCOME</h1>


      <p>The portal will allow you to create and manage events</p>

      </div>

    </div>
  );
};

export default Welcome;