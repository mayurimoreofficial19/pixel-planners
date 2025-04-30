import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import VerifyEmail from "./pages/Account/VerifyEmail/VerifyEmail";
import ResetPassword from "./pages/Account/ResetPassword/ResetPassword";
import Dashboard from "./pages/Dashboard/Dashboard";
import VenuePage from "./pages/Venues/VenuePage";
import VendorPage from "./pages/Vendors/VendorPage";
import ClientPage from "./pages/Clients/ClientPage";
import GuestPage from "./pages/Guests/GuestPage";
import "./App.css";
import Welcome from "./pages/Welcome/Welcome";
import OAuth2RedirectHandler from "./pages/User/OAuth2RedirectHandler";
import UserProfile from "./pages/User/UserProfile";

// Protected Route component
const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  return children;
};

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <Routes>
            <Route path="/" element={<Welcome />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/verify-email" element={<VerifyEmail />} />
          <Route path="/reset-password" element={<ResetPassword />} />
          <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
                    <Route
                      path="/profile"
                      element={
                        <ProtectedRoute>
                          <UserProfile />
                        </ProtectedRoute>
                      }
                    />
          <Route path="/venues" element={<VenuePage />} />
          <Route path="/vendors" element={<VendorPage />} />
          <Route path="/clients" element={<ClientPage />} />
          <Route path="/guests" element={<GuestPage />} />
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
