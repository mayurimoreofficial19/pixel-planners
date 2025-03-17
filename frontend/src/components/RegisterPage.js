import React, { useState } from 'react';
import axios from 'axios';
import Header from './Header';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [email, setEmail] = useState('');
  const [verifyEmail, setVerifyEmail] = useState('');
  const [error, setError] = useState('');


  const handleSubmit = async (e) => {
    e.preventDefault();

        if (password !== verifyPassword) {
          setError('Passwords do not match');
          return;
        }
        if (email !== verifyEmail) {
          setError('Emails do not match');
          return;
        }
    try {
      const response = await axios.post('/register', { username, password, verifyPassword, email, verifyEmail });
      // Handle successful registration
    } catch (error) {
      // Handle registration error
    }
  };

  return (
    <div>
        <Header />
      <h1>Register</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <div>
          <label>Verify Password:</label>
          <input type="password" value={verifyPassword} onChange={(e) => setVerifyPassword(e.target.value)} />
        </div>
        <div>
          <label>Email:</label>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div>
          <label>Verify Email:</label>
          <input type="email" value={verifyEmail} onChange={(e) => setVerifyEmail(e.target.value)} />
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default RegisterPage;