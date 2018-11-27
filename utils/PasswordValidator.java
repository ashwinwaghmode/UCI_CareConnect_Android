package com.devool.ucicareconnect.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;
    static final String SPECIAL_CHARACTERS = "!,#,$,%,^,&,*,|";

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validPassword(String password){
        boolean upCase = false;
        boolean loCase = false;
        boolean isDigit = false;
        boolean spChar = false;
        if (password.length()>8){
            upCase = true;
            if (password.matches(".+[A-Z].+")){
                upCase = true;
            }
            if (password.matches(".+[a-z].+")){
                loCase = true;
            }
            if (password.matches(".+[1-9].+")){
                isDigit = true;
            }
            if (SPECIAL_CHARACTERS.contains(password)){
                spChar = true;
            }
        }
        return (upCase && loCase && isDigit && spChar);
    }
}
