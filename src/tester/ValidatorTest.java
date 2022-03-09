package tester;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import utilities.Validator;

public class ValidatorTest {
    @Test
    public void validateDigits() {
        assertTrue(Validator.validateDigits("123"));
        assertFalse(Validator.validateDigits("input"));
    }

    @Test
    public void validateEmail() {
        assertTrue(Validator.validateEmail("@"));
        assertFalse(Validator.validateEmail("testattest.com"));
    }

    @Test
    public void validateGenre() {
        assertTrue(Validator.validateGenre("movie"));
        assertFalse(Validator.validateGenre("1"));
    }

    @Test
    public void validateCategory() {
        assertTrue(Validator.validateCategory("Book"));
        assertFalse((Validator.validateCategory("input")));
    }

    @Test
    public void validateDate() {
        assertFalse(Validator.validateDate("input"));
        assertFalse(Validator.validateDate("1999/10/12"));
        assertFalse(Validator.validateDate("1999-13-12"));
        assertTrue(Validator.validateDate("1999-12-12"));
    }
}
