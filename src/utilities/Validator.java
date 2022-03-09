package utilities;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * <h2>Validator</h2>
 *
 * This class is for checking validation credentials for the different text
 * fields.
 * All of these methods checks if the input is null, triggering an error message
 * in another class
 *
 * @author Haykal Y. Mohamud
 * 
 */
public class Validator {
    /**
     * This method receives the user's input and checks if it passes the conditions
     * that are in place:making sure
     * the input is null, and also making sure that only digits are accepted.
     *
     * @param input //The user's input into the text field
     * @return //true or false depending on whether the condition's met
     */
    public static boolean validateDigits(String input) {
        if (input == null)
            return false;
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * This method receives the input and checks whether the character "@" is
     * contained in the user input
     *
     * @param input //The user's input into the text field
     * @return //true or false depending on whether the condition's met
     */
    public static boolean validateEmail(String input) {
        if (input == null)
            return false;
        if (input.contains("@"))
            return true;
        return false;
    }

    /**
     * This method receives the user's input and makes sure that the input follows
     * yyyy-MM-dd format
     * 
     * @param input //The user's input into the text field
     * @return //true or false depending on whether the condition's met
     */
    public static boolean validateDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(input, new ParsePosition(0)) != null;
    }

    /**
     * This method receives a user's input and checks if the input contains any
     * digits.
     * If the condition is breached and error handling message occurs
     *
     * @param input //The user's input into the text field
     * @return //true or false depending on whether the condition's met
     */
    public static boolean validateGenre(String input) {
        return !validateDigits(input);
    }

    /**
     * This method receives a user's input and checks whether any of those inputs
     * falls
     * under the different category.
     *
     * @param input //The user's input into the text field
     * @return //true or false depending on whether the condition's met
     */
    public static boolean validateCategory(String input) {
        if (input.equalsIgnoreCase("movie") || (input.equalsIgnoreCase("book")) || (input.equalsIgnoreCase("magazine")))
            return true;
        return false;
    }

}
