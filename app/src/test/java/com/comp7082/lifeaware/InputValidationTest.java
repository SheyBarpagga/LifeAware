package com.comp7082.lifeaware;

import org.junit.Test;
import static org.junit.Assert.*;


public class InputValidationTest {

    InputValidation validation = new InputValidation();
    @Test
    public void emptyStringValid() {
        String testString = "";
        assertTrue(validation.emptyString(testString));
    }

    @Test
    public void emptyStringInvalid() {
        String testString = "test";
        assertFalse(validation.emptyString(testString));
    }

    @Test
    public void emailStringValid() {
        String testString = "sami@hotmail.com";
        assertTrue(validation.validEmail(testString));
    }

    @Test
    public void emailStringInvalid() {
        String testString = "test";
        assertFalse(validation.validEmail(testString));
    }

    @Test
    public void ageIntValid() {
        int testString = 18;
        assertTrue(validation.validAge(testString));
    }

    @Test
    public void ageIntInvalid() {
        int testString = 121;
        assertFalse(validation.validAge(testString));
    }

    @Test
    public void passwordStringValid() {
        String testString = "fanA123@dsa";
        assertTrue(validation.validPassword(testString));
    }

    @Test
    public void passwordStringInvalid() {
        String testString = "121";
        assertFalse(validation.validPassword(testString));
    }
}
