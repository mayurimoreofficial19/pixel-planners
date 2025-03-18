import React, { createContext, useState, useEffect, useContext } from 'react';
import { login, getCurrentUser, logout } from '../services/AuthService';

export const AuthenticationContext = createContext();

export const useAuth = () => {
  return useContext(AuthenticationContext);
};

export const AuthenticationProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const user = await getCurrentUser();
        setCurrentUser(user);
      } catch (error) {
        setCurrentUser(null);
      } finally {
        setLoading(false);
      }
    };

    fetchCurrentUser();
  }, []);

  const handleLogin = async (username, email, password) => {
    try {
      const user = await login(username, email, password);
      setCurrentUser(user);
    } catch (error) {
      throw new Error('Login failed');
    }
  };

  const handleLogout = async () => {
    try {
      await logout();
      setCurrentUser(null);
    } catch (error) {
      throw new Error('Logout failed');
    }
  };

  return (
    <AuthenticationContext.Provider value={{ currentUser, loading, login: handleLogin, logout: handleLogout }}>
      {children}
    </AuthenticationContext.Provider>
  );
};