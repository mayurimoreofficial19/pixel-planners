import React, { useState } from 'react';
import axios from 'axios';
import Header from './Header';
//import 'index.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/login', { username, password });
      // Handle successful login
    } catch (error) {
      // Handle login error
    }
  };

  return (
    <div>
    <Header />
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit">Login</button>
      </form>
      <p class="mt-5">Don't have an account? <a href="/register">Register here.</a></p>
    </div>
  );
};

export default LoginPage;