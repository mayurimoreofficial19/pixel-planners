import React, { useState } from 'react';
import { resetPassword } from '../service/UserService';
import Header from '../components/Header';

const ResetPasswordPage = () => {
  const [username, setUsername] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await resetPassword(username, newPassword, verifyPassword);
      setSuccess('Password reset successfully!');
      setError('');
    } catch (error) {
      setError('Failed to reset password. Please try again.');
      setSuccess('');
    }
  };

  return (
    <div>
      <Header />
      <h1>Reset Password</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label>New Password:</label>
          <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
        </div>
        <div>
          <label>Verify Password:</label>
          <input type="password" value={verifyPassword} onChange={(e) => setVerifyPassword(e.target.value)} />
        </div>
        <button type="submit">Reset Password</button>
      </form>
      {error && <p>{error}</p>}
      {success && <p>{success}</p>}
    </div>
  );
};

export default ResetPasswordPage;