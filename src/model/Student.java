package model;

import exception.InvalidInputException;

public class Student extends PersonBase {

    private String level;
    private int discountPercent;

    public Student(int id, String name, String email, String level, int discountPercent) {
        super(id, name, email);
        setLevel(level);
        setDiscountPercent(discountPercent);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public double calculateMonthlyPayOrDiscountValue() {
        return discountPercent;
    }

    @Override
    public void validate() {
        super.validate();
        if (level == null || level.trim().isEmpty())
            throw new InvalidInputException("Student level must not be empty");
        if (discountPercent < 0 || discountPercent > 50)
            throw new InvalidInputException("Discount percent must be 0..50");
    }

    public String getLevel() { return level; }
    public void setLevel(String level) {
        if (level == null || level.trim().isEmpty())
            throw new InvalidInputException("Student level must not be empty");
        this.level = level.trim();
    }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 50)
            throw new InvalidInputException("Discount percent must be 0..50");
        this.discountPercent = discountPercent;
    }
}
