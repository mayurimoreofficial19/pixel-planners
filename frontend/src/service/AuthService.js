import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user';

export const register = async (username, email, verifyEmail, password, verifyPassword ) => {

    const userData = {
        username,
        email,
        password,
        verifyEmail,
        verifyPassword
    };

    try {
        const response = await axios.post(`${API_URL}/register`, userData, {
            withCredentials: true,
            headers: { 'Content-Type': 'application/json' }
        });

        console.log("Registration Response: ", response.data, response.status);

        return response.data;
    } catch (error) {
        const errorData = error.response.data;
        let allDefaultMessages = [];

        // Add all "defaultMessage" from error response to empty array to be logged in console
        for (let i = 0; i < errorData.length; i++) {
            allDefaultMessages.push(errorData[i].defaultMessage);

            alert(errorData[i].defaultMessage);
        }

        console.error("Error registering user!", allDefaultMessages);
        throw error;
    };
}
export const login = async (username, email, password) => {
    const userData = {
        username,
        email,
        password
    };

    try {
        const response = await axios.post(`${API_URL}/login`, userData, {
            withCredentials: true,
            headers: { 'Content-Type': 'application/json' }
    });
        // Log entire response object
        console.log("Login Response: ", response.data, response.status);

        return response.data;
    } catch (error) {
        const errorData = error.response.data;
        let allDefaultMessages = [];

        // Add all "defaultMessage" from error response to empty array to be logged in console
        for (let i = 0; i < errorData.length; i++) {
            allDefaultMessages.push(errorData[i].defaultMessage);

            alert(errorData[i].defaultMessage);
        }

        console.error("Error logging in user!", allDefaultMessages);
        throw error;
    };

};

export const getCurrentUser = async () => {
    try {
        const response = await axios.get (`${API_URL}/currentUser`, {
            headers: { 'Content-Type': 'application/json' },
            withCredentials: true
        });
        const user = response.data;

        return user;
    } catch (error) {
        console.error('No current user found!', error);
        return null;
    };
};

export const logout = async () => {
    try {
        const response = await axios.get(`${API_URL}/logout`, {
            withCredentials: true
        });

        console.log("Logout response: " + response.data, response.status);

        return response.data;
    } catch (error) {
        console.error('Failed to logout!');
        throw error;
    };
};