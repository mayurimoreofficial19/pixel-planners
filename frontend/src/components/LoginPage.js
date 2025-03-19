import React, { useState, useEffect } from 'react';
import { login } from '../service/AuthService'; // Import the login function
import axios from 'axios';
import Header from './Header';
//import 'index.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [emailAddress, setEmailAddress] = useState('');

  const [showPassword, setShowPassword] = useState(false);

      useEffect(() => {
          setError('');
      }, [])

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await login(username, emailAddress, password);
            window.location.reload(); // Reload to update context
            setError('');
            alert(`${username} has logged in!`);
            window.location.href = "/";
        } catch (error) {
            setError('Failed to login. Please try again!');
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
                  <label>Email:</label>
                  <input type="email" value={emailAddress} onChange={(e) => setEmailAddress(e.target.value)} />
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