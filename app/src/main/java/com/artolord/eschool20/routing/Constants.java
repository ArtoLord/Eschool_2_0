package com.artolord.eschool20.routing;

public  class Constants {
    public final static String url = "https://app.eschool.center/ec-server/";
    public final static String username = "username";
    public final static String password = "password";
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
