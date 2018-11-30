package com.artolord.eschool20.routing;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Login: ");
        String login = in.next();
        System.out.print("Password: ");
        String password = in.next();
        Route route = new Route();
        route.login(login,password);



    }
}
