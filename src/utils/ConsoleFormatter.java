package utils;

public class ConsoleFormatter {

    public static void title(String text) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + text.toUpperCase());
        System.out.println("========================================");
    }

    public static void success(String message) {
        System.out.println("[ OK ] " + message);
    }

    public static void error(String message) {
        System.out.println("[ ERROR ] " + message);
    }

    public static void info(String message) {
        System.out.println("[ INFO ] " + message);
    }

    public static void separator() {
        System.out.println("----------------------------------------");
    }
}
