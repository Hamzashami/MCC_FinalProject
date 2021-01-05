package com.hamzashami.coronaproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class isChecked {
    private  Pattern pattern = Pattern.compile(android.util.Patterns.EMAIL_ADDRESS.pattern());

    public  boolean validateEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validateUsername(String username){
        return username.length()>6;
    }
    public boolean validatePassword(String password){
        return password.length()>8;
    }
    public boolean vaidateConfirmPassword(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }
}
