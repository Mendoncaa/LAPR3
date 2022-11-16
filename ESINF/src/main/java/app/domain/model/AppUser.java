package app.domain.model;


import app.domain.shared.UserAttributes;
import java.util.Objects;

public class AppUser {

    private String name;
    private String password;
    private String role;
    private String email;
    private long Id;

    static long IdCounter = 1;

    public AppUser(String name, String email, String role) {
        this.name = name;
        this.password = UserAttributes.passwordCreation();
        this.role = role;
        this.email = email;
        this.Id = IdCounter++;
    }

    public AppUser() {
    }

    @Override
    public String toString() {
        return String.format("Name : "+this.name+"   Role : "+this.role+"   ID : "+this.Id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Id == appUser.Id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, role, email, Id);
    }
}