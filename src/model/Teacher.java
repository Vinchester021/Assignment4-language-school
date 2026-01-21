package model;

import exception.InvalidInputException;

public class Teacher extends PersonBase {

    private String specialization;
    private double salaryPerMonth;

    public Teacher(int id, String name, String email, String specialization, double salaryPerMonth) {
        super(id, name, email);
        setSpecialization(specialization);
        setSalaryPerMonth(salaryPerMonth);
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }

    @Override
    public double calculateMonthlyPayOrDiscountValue() {
        return salaryPerMonth;
    }

    @Override
    public void validate() {
        super.validate();
        if (specialization == null || specialization.trim().isEmpty())
            throw new InvalidInputException("Specialization must not be empty");
        if (salaryPerMonth <= 0)
            throw new InvalidInputException("Salary must be > 0");
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty())
            throw new InvalidInputException("Specialization must not be empty");
        this.specialization = specialization.trim();
    }

    public double getSalaryPerMonth() { return salaryPerMonth; }
    public void setSalaryPerMonth(double salaryPerMonth) {
        if (salaryPerMonth <= 0)
            throw new InvalidInputException("Salary must be > 0");
        this.salaryPerMonth = salaryPerMonth;
    }
}
