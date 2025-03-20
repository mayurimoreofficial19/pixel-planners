import React, { useState, useEffect } from 'react';
import { register } from '../service/AuthService'; // Import the register function from AuthService
import { registerUser, fetchUsers } from '../service/UserService'; // Import the createUser and fetchUsers functions from UserService
//import axios from 'axios';
import Header from './Header';
import { Link, useNavigate } from 'react-router-dom';
//import { useAuth } from '../context/AuthenticationContext';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [email, setEmail] = useState('');
  const [verifyEmail, setVerifyEmail] = useState('');
  const [error, setError] = useState('');
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  //const {user, isAuthenticated} = useAuth();

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

    const goodValues = username && password && verifyPassword && email && verifyEmail;
    if (!goodValues) {
        setError('Please fill out all fields');
        alert('Please fill out all fields');
        return;
    }

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

    //Password must be between 3 and 30 characters

      if (password.length < 3 || password.length > 30) {
      alert('Passwords must be between 3 and 30 characters!');

      return;
      }


        //checking if username or email is already taken
        const isUsernameTaken = users.some(user => user.username === username);
        if (isUsernameTaken) {
          setError('Username is already taken');
          alert('Username is already taken');
          return;
        }

        const isEmailTaken = users.some(user => user.email === email);
        if (isEmailTaken) {
            setError('Email is already taken');
            alert('Email is already taken');
            return;
        }

//    if (username !== '' && email !== '' && password !== '' && verifyPassword !== '' && verifyEmail !== '' && goodValues) {
//    setError('');
//    registerUser(username, email, verifyEmail, password, verifyPassword);
//      alert('Registration successful');
//      navigate('/login'); // Redirect to login page after successful registration
//    } else {
//    setError ("User is not registered. Please try again.")}


    try {
    await register(username, email, password);
        await registerUser(username, email, verifyEmail, password, verifyPassword);
        alert('Registration successful');
        navigate('/user'); // Redirect to login page after successful registration
    } catch (error) {
      setError('Registration failed');
      alert('Registration failed');
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
        <div>{error}</div>
        <p>
          Already have an account? <Link to="/login">Login</Link>
        </
        p>
      </form>
    </div>

  );
};

export default RegisterPage;