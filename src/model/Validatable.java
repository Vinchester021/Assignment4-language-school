package model;

import exception.InvalidInputException;

public interface Validatable {

    void validate();

    default void requireNotBlank(String value, String fieldName) {
        if (isBlank(value)) {
            throw new InvalidInputException(fieldName + " must not be empty");
        }
    }

    static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
