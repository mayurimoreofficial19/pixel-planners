import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthenticationContext';
import Header from '../components/Header';
import { useNavigate } from 'react-router-dom';
import { deleteUser, updateUser } from '../service/UserService';
import UpdateUserPage from './UpdateUserPage';

const UserPage = () => {
  const { currentUser, isAuthenticated } = useAuth();
  const [user, setUser] = useState(null);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const [editMode, setEditMode] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      setUser(currentUser);
      setUsername(currentUser.username);
      setEmail(currentUser.email);
    }
  }, [isAuthenticated, currentUser]);

  const handleUpdate = async (e) => {
    e.preventDefault();

// eslint-disable-next-line no-restricted-globals
    if (!confirm(`Are you sure you want to update your profile?`)) {
      // Cancel is clicked
        e.preventDefault();
      alert('Cancelled: Profile was NOT updated!');
    } else { // Ok is clicked
    setEditMode(true);
    }
    };

  const handleDelete = async (e) => {
    e.preventDefault(); // Prevent the default form submission

    // eslint-disable-next-line no-restricted-globals
    if (!confirm(`Are you sure you want to delete user: ${user.username}?`)) {
      // Cancel is clicked
      e.preventDefault();
      alert('Cancelled: User was NOT deleted!');
    } else { // Ok is clicked
    try {
      await deleteUser(user.id);
      alert(`${user.username} has been deleted!`);
      window.location.href = "/";
    } catch (error) {
      console.error('Failed to delete user!', error);
      throw error;
    }
    }
    };

  if (!isAuthenticated) {
    return <p>Please log in to view your profile.</p>;
  }

  return (
  <>
    {!editMode? (
    <div>
      <Header />
      <h1>User Profile</h1>
      {user ? (
        <div>
          <div>
            <label>Username:</label>
            <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
          </div>
          <div>
            <label>Email:</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div>
          <label>Password:</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
            </div>
          <button onClick={handleUpdate}>Update Profile</button>
          <button onClick={handleDelete}>Delete Profile</button>
            </div>
          ) : (
            <p>Loading...</p>
          )}
        </div>
      ) : (
        <UpdateUserPage />
      )}
      {error && <p>{error}</p>}
    </>
  );
};

export default UserPage;
