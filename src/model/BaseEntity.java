package model;

import exception.InvalidInputException;

public abstract class BaseEntity implements Identifiable {

    protected int id;
    protected String name;

    protected BaseEntity(int id, String name) {
        this.id = id;
        setName(name);
    }

    public abstract String getType();
    public abstract void validate();
    public String shortInfo() {
        return getType() + " #" + id + " " + name;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name must not be empty");
        this.name = name.trim();
    }
}

