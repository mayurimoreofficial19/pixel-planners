import React, { useState } from 'react';
import { useAuth } from '../context/AuthenticationContext';
import Header from '../components/Header';
import { useNavigate } from 'react-router-dom';
import { deleteUser, updateUser } from '../service/UserService';
import { Link } from 'react-router-dom';

const UpdateUserPage = () => {
    const { currentUser, isAuthenticated } = useAuth();
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [verifyEmail, setVerifyEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');
    const navigate = useNavigate();

        const handleSubmit = async (e) => {
            e.preventDefault();

            try {
                await updateUser(username, email, verifyEmail, password, verifyPassword);
                window.location.reload(); // Reload to update context
                //setError('');
                alert(`${username} was successfully updated!`)

            } catch (error) {
                setError("User was not updated. Please try again.");
            }

        };

          const handleDelete = async (e) => {
            e.preventDefault();

// eslint-disable-next-line no-restricted-globals
            if (!confirm(`Are you sure you want to delete user: ${username}?`)) {
              // Cancel is clicked
              e.preventDefault();
              alert('Cancelled: User was NOT deleted!');
            } else {
              // Ok is clicked
              try {
                await deleteUser(currentUser.id);
                alert(`${username} has been deleted!`);
                window.location.href = "/";
                navigate('/register');
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
           <div>
               <Header />
               <h1>Update User Profile</h1>
               <form onSubmit={handleSubmit}>
                   <div>
                       <label>Username:</label>
                       <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                   </div>
                   <div>
                       <label>Email:</label>
                       <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                   </div>
                   <div>
                       <label>Verify Email:</label>
                       <input type="email" value={verifyEmail} onChange={(e) => setVerifyEmail(e.target.value)} />
                   </div>
                   <div>
                       <label>Password:</label>
                       <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                   </div>
                   <div>
                       <label>Verify Password:</label>
                       <input type="password" value={verifyPassword} onChange={(e) => setVerifyPassword(e.target.value)} />
                   </div>
                   <button type="submit">Update</button>
                   <button type="button" onClick={handleDelete}>Delete</button>
                   <Link to="/user">Back to Profile</Link>
               </form>
               {error && <p>{error}</p>}
           </div>
       );
   };

   export default UpdateUserPage;