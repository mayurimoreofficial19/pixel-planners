import React, { createContext, useState, useEffect, useContext } from 'react';
import { login, getCurrentUser, logout } from '../service/AuthService';

export const AuthenticationContext = createContext();

export const useAuth = () => {
  return useContext(AuthenticationContext);
};

export const AuthenticationProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const user = await getCurrentUser();
        setCurrentUser(user);
        setIsAuthenticated(true);
        console.log('Current User: ' + currentUser.username);
      } catch (error) {
        setCurrentUser(null);
        setIsAuthenticated(false);
      } finally {
        setLoading(false);
      }
    };

    fetchCurrentUser();
  }, []);


  const handleLogin = async (username, emailAddress, password) => {
    try {
      const user = await login(username, emailAddress, password);
      setCurrentUser(user);
      setIsAuthenticated(true);
      console.error("Failed to login user!");
    } catch (error) {
      throw new Error('Login failed');
    }
  };

  const handleLogout = async () => {
    try {
      await logout();
      setCurrentUser(null);
      setIsAuthenticated(false);
    } catch (error) {
      throw new Error('Logout failed');
    }
  };

  return (
    <AuthenticationContext.Provider value={{ currentUser, loading, isAuthenticated, login: handleLogin, logout: handleLogout }}>
      {children}
    </AuthenticationContext.Provider>
  );
};

export default AuthenticationContext;