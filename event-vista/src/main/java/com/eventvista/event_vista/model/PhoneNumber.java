package com.eventvista.event_vista.model;

public class PhoneNumber {

    private static boolean validatePhoneNumber(String phoneNumber) {
        // Validate phone numbers of format "1234567890" or "+11234567890"
        if (phoneNumber.matches("\\+\\d(-\\d{3}){2}-\\d{4}"))
            return true;
            // Validating phone number with -, . or spaces
        else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
            // Validating phone number with extension length from 3 to 5
        else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
            // Validating phone number where area code is in braces ()
        else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
            // Validation for India numbers
        else if (phoneNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
            return true;
        else if (phoneNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
            return true;
        else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;
            // Return false if nothing matches the input
        else
            return false;
    }
}


