package coffeeshop.client;

import java.util.Scanner;

// Entry point.
//   no arguments -> interactive coffee shop
//   "demo"       -> scripted demo of every pattern
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "demo".equalsIgnoreCase(args[0])) {
            new Demo().run();
        } else {
            new ConsoleApp(new Scanner(System.in)).run();
        }
    }
}
