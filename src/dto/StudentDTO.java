package dto;

public class StudentDTO {

    private int id;
    private String name;
    private String email;
    private String level;

    public StudentDTO(int id, String name, String email, String level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getLevel() { return level; }
}