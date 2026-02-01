package model;

import exception.InvalidInputException;

public class Course implements Validatable, Identifiable {

    private int id;
    private String name;
    private String level;
    private double price;
    private int teacherId;

    public Course(int id, String name, String level, double price, int teacherId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.price = price;
        this.teacherId = teacherId;
        validate();
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Course name must not be empty");
        if (level == null || level.trim().isEmpty())
            throw new InvalidInputException("Course level must not be empty");
        if (price <= 0)
            throw new InvalidInputException("Course price must be > 0");
        if (teacherId <= 0)
            throw new InvalidInputException("TeacherId must be > 0");
    }

    @Override public int getId() { return id; }

    public String getName() { return name; }
    public String getLevel() { return level; }
    public double getPrice() { return price; }
    public int getTeacherId() { return teacherId; }

    public void setName(String name) { this.name = name; validate(); }
    public void setLevel(String level) { this.level = level; validate(); }
    public void setPrice(double price) { this.price = price; validate(); }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; validate(); }
}
