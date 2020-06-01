package com.ngomalalibo.stocktradingapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomEmailValidator
{
    /*public static boolean validIdValue(String objectId)
    {
        boolean isValid;
        
        if (!objectId.equals(""))
        {
            isValid = true;
        }
        else
        {
            throw new IllegalArgumentException("Enter a isValid Id");
            
        }
        return isValid;
    }*/
    
    public static boolean validEmailValue(String email)
    {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }
}
