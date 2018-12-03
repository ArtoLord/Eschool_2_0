package com.artolord.eschool20.routing;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public  class Constants {
    public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    public final static String AppPref = "app_pref";
    public final static String CookiePref = "cookie_pref";

    public final static String url = "https://app.eschool.center/ec-server/";
    public final static String username = "username";
    public final static String password = "password";
    public final static Integer LoginError  = 1;
    public final static Integer StateError  = 2;
    public final static Integer PeriodError  = 3;
    public final static Integer UnitError  = 4;
    public final static Integer CookieError  = 5;
    public final static Integer MarkError  = 5;

}
