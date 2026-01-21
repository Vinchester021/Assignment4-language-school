package model;

import exception.InvalidInputException;

public abstract class PersonBase implements Validatable, Identifiable {

    protected int id;
    protected String name;
    protected String email;

    protected PersonBase(int id, String name, String email) {
        this.id = id;
        setName(name);
        setEmail(email);
    }

    public abstract String getRole();
    public abstract double calculateMonthlyPayOrDiscountValue();

    public String shortInfo() {
        return getRole() + " #" + id + " " + name + " <" + email + ">";
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name must not be empty");
        if (email == null || email.trim().isEmpty() || !email.contains("@"))
            throw new InvalidInputException("Email must be valid");
    }

    @Override
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name must not be empty");
        this.name = name.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@"))
            throw new InvalidInputException("Email must be valid");
        this.email = email.trim();
    }
}
