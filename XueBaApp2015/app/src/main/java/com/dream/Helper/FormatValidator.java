package com.dream.Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 夏目 on 2015/11/8.
 */
public class FormatValidator {
    private static String emailValidator = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*" ;
    private static String passwordValidator = "\\w{6,20}";
    //private static String nickValidator = "^[\\w\u4e00-\u9fa5]{1,20}";

    public static boolean ValidateEmail(String email)
    {
        //return emailValidator.IsMatch(email);
        boolean result = false ;
        Pattern emailPatt = Pattern.compile(emailValidator) ;
        Matcher matc = emailPatt.matcher(email) ;
        result = matc.matches() ;
        return result ;
    }

    public static boolean ValidatePassword(String password)
    {
        boolean result = false ;
        Pattern pwPatt = Pattern.compile(passwordValidator) ;
        Matcher matc = pwPatt.matcher(password) ;
        result = matc.matches() ;
        return result ;
    }
/*
    public static boolean ValidateNick(String nick)
    {
        boolean result = false ;
        Pattern nickPatt = Pattern.compile(nickValidator) ;
        Matcher matc = nickPatt.matcher(nick) ;
        result = matc.matches() ;
        return result ;
    }*/
}
