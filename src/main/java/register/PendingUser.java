package register;

public class PendingUser {
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String skills;
    private String email;

    public PendingUser(String username, String password, String fullname, String phone, String skills, String email) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.skills = skills;
        this.email = email;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullname() { return fullname; }
    public String getPhone() { return phone; }
    public String getSkills() { return skills; }
    public String getEmail() { return email; }
}
