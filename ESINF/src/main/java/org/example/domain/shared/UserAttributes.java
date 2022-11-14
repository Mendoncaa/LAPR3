package org.example.domain.shared;

import org.apache.commons.lang3.StringUtils;
import org.example.controller.App;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAttributes {

    private static final int PASSWORDLENGTH = 7;
    private static final int PASSWORDNUMBERS = 2;
    private static final int PASSWORDUPPERCASELETTERS = 3;

    /**
     * method that starts the creation of a password
     *
     * @return String password
     */
    public static String passwordCreation() {

        String password;

        do {
            password = getRandomPassword(PASSWORDLENGTH); //get a random password with the length that the client specified
        } while (!checkPasswordWithNoError(password));

        return password;

    }

    /**
     * method that has the functionality to build a completely random and new password.
     *
     * @param length number of elements for the password
     * @return String password
     */
    private static String getRandomPassword(int length) {

        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, StandardCharsets.UTF_8); //implements just the characters from UTF-8
        StringBuilder password = new StringBuilder();

        for (int k = 0; k < randomString.length(); k++) {

            char character = randomString.charAt(k);

            if (((character >= 'a' && character <= 'z')
                    || (character >= 'A' && character <= 'Z')   //UTF-8 doesn't have just alphanumeric elements, so we had to limit the option with this function
                    || (character >= '0' && character <= '9'))
                    && (length > 0)) {

                password.append(character); // append the character with the restant password already or not created
                length--; // do this until the length is zero
            }
        }

        return password.toString();
    }


    /**
     * do the same as the checkPassword method but don't throw error if the password inserted there is not correct
     *
     * @param password String password
     * @return if the password is correctly created
     */

    public static boolean checkPasswordWithNoError(String password) {

        if (password == null) {
            return false;
        }

        int numbers = 0;
        int upperCase = 0;
        int lowerCase = 0;

        Character[] passChars = new Character[password.length()];
        StringCharacters(password, passChars);

        for (Character passChar : passChars) {
            if (Character.isLowerCase(passChar)) {
                lowerCase++;
            }
            if (Character.isUpperCase(passChar)) {
                upperCase++;
            }
            if (Character.isDigit(passChar)) {
                numbers++;
            }
        }

        return numbers + upperCase + lowerCase == PASSWORDLENGTH && numbers == PASSWORDNUMBERS && upperCase == PASSWORDUPPERCASELETTERS;
    }

    /**
     * method that fills an array with a String letter by letter
     *
     * @param string   String to be decomposed
     * @param pswChars array with all the characters
     */

    private static void StringCharacters(String string, Character[] pswChars) {
        for (int i = 0; i < string.length(); i++) {
            pswChars[i] = string.charAt(i);
        }
    }

    /**
     * method that verifies if the email is valid
     *
     * @param email user's email
     */
    public static boolean emailVerification(String email) {

        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            }

        }
        return false;
    }

    public static boolean validateEmployeeAttributes(String name, String email, String role, int phoneNumber, int citizenCardNumber, String address) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if (!emailVerification(email) && containsEmail(email)) {
            return false;
        }
        if (StringUtils.isBlank(role)) {
            return false;
        }
        if (String.valueOf(phoneNumber).length() != 9) {
            return false;
        }
        if (String.valueOf(citizenCardNumber).length() != 8) {
            return false;
        }

        if (StringUtils.isBlank(address)) {
            return false;
        }

        return true;
    }


    public static boolean isDateValid(String strDate) {
        String dateFormat2 = "d/M/uuuu";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat2)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate date = LocalDate.parse(strDate, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean containsEmail(String email){
        for (int i = 0; i < App.getInstance().getEmailsPasswords().size(); i++) {
            if (email.equalsIgnoreCase(App.getInstance().getEmailsPasswords().getEmailByID(i))){
                return true;
            }
        }
        return false;
    }

    public static boolean validateUser(String email) {
        return containsEmail(email);
    }
}