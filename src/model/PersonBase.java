package model;

import exception.InvalidInputException;

public abstract class PersonBase extends BaseEntity implements Validatable {

    protected String email;

    protected PersonBase(int id, String name, String email) {
        super(id, name);
        setEmail(email);
    }

    public abstract String getRole();
    public abstract double calculateMonthlyPayOrDiscountValue();

    @Override
    public String getType() {
        return getRole();
    }

    @Override
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

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@"))
            throw new InvalidInputException("Email must be valid");
        this.email = email.trim();
    }
}
