package com.artolord.eschool20.routing;


import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Login: ");
        String login = in.next();
        System.out.print("Password: ");
        String password = in.next();
        Route route = new Route();
        //String sha256hex = Constants.bin2hex(password.getBytes());
        //route.login(login,sha256hex);



    }
}
