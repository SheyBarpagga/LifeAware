package com.comp7082.lifeaware;

import android.content.Context;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class InputValidation
{
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";


    public InputValidation() {}


    public boolean inputTextFieldEditEmpty(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        String input = textInputEditText.getText().toString().trim();
        if (emptyString(input)) {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean emptyString(String message)
    {
        return message.length() == 0;
    }

    public boolean inputTextEmailEditValid(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        String input = textInputEditText.getText().toString().trim();
        if (emptyString(input) || !validEmail(input))
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validEmail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean inputTextAgeValid(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        int input = Integer.parseInt(textInputEditText.getText().toString());
        if (!validAge(input))
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validAge(int age)
    {
        return age > 17 && age < 121;
    }
    public boolean inputTextPasswordEditValid(EditText textInputEditText,
                                              TextInputLayout textInputLayout,
                                              String message)
    {
        String input = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(input);
        if (!validPassword(input))
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validPassword(String password)
    {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

//    public boolean inputEditTextMatches(TextInputEditText textInputEditText1,
//                                        TextInputEditText textInputEditText2,
//                                        TextInputLayout textInputLayout,
//                                        String message)
//    {
//        String value1 = textInputEditText1.getText().toString().trim();
//        String value2 = textInputEditText2.getText().toString().trim();
//        if (!value1.contentEquals(value2)) {
//            textInputLayout.setError(message);
//            return false;
//        } else {
//            textInputLayout.setErrorEnabled(false);
//        }
//        return true;
//    }
}
