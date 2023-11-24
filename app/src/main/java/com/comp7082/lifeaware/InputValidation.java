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

    private Context context;

    public InputValidation(Context context)
    {
        this.context = context;
    }

    public boolean inputTextFieldEditEmpty(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        String input = textInputEditText.getText().toString().trim();
        if (input.isEmpty()) {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean inputTextEmailEditValid(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        String input = textInputEditText.getText().toString().trim();
        if (input.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches())
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean inputTextAgeValid(EditText textInputEditText,
                                           TextInputLayout textInputLayout,
                                           String message)
    {
        int input = Integer.parseInt(textInputEditText.getText().toString());
        if (input > 120 || input < 18)
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
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
        if (!matcher.matches())
        {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean inputEditTextMatches(TextInputEditText textInputEditText1,
                                        TextInputEditText textInputEditText2,
                                        TextInputLayout textInputLayout,
                                        String message)
    {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
}
