import React, { useState, useEffect } from 'react';
import { register } from '../service/AuthService'; // Import the register function from AuthService
import { registerUser, fetchUsers } from '../service/UserService'; // Import the createUser and fetchUsers functions from UserService
import axios from 'axios';
import Header from './Header';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [email, setEmail] = useState('');
  const [verifyEmail, setVerifyEmail] = useState('');
  const [error, setError] = useState('');
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchAllUsers = async () => {
      try {
        const usersData = await fetchUsers();
        setUsers(usersData);
      } catch (error) {
        console.error('Failed to fetch users', error);
      }
    };

    fetchAllUsers();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== verifyPassword) {
      setError('Passwords do not match');
      alert('Passwords do not match');
      return;
    }
    if (email !== verifyEmail) {
      setError('Emails do not match');
      alert('Emails do not match');
      return;
    }

    const isUsernameTaken = users.some(user => user.username === username);
    const isEmailTaken = users.some(user => user.email === email);

    if (isUsernameTaken) {
      setError('Username is already taken');
      alert('Username is already taken');
      return;
    }

    if (isEmailTaken) {
      setError('Email is already taken');
      alert('Email is already taken');
      return;
    }

              //Password must be between 5 and 30 characters
    if (password) {
      if (password.length < 5 || password.length > 30) {
      alert('Passwords must be between 5 and 30 characters!');
      e.preventDefault();
      return false;
      }
    }

    try {
      await register(username, email, password); // Use the register function from AuthService
      await registerUser(username, email); // Use the createUser function from UserService
      alert('Registration successful');
      // Handle successful registration
    } catch (error) {
      setError('Registration failed');
      alert('Registration failed');
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