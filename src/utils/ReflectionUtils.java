package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void printClassInfo(Class<?> clazz) {
        System.out.println("Reflection info for class: " + clazz.getSimpleName());

        System.out.println("Fields:");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println(" < " + field.getType().getSimpleName()
                    + " " + field.getName() + " > ");
        }

        System.out.println("\nMethods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println(" < " + method.getName() + " > ");
        }

        System.out.println("\n");
    }
}
