import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const OAuth2RedirectHandler = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    // Parse the URL query parameters
    const params = new URLSearchParams(location.search);
    const token = params.get("token");
    const error = params.get("error");

    if (token) {
      // Save the token (localStorage or context, etc.)
      localStorage.setItem("token", token);
      // Redirect to the dashboard
      navigate("/dashboard");
    } else if (error) {
      console.error("OAuth error:", error);
      // Optionally, show error message or navigate to login
      navigate("/login", { state: { error } });
    }
  }, [location, navigate]);

  return <div>Redirecting...</div>;
};

export default OAuth2RedirectHandler;
