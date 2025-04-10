package com.example.redcross;


import jakarta.persistence.*;
//JPA annotations

@Entity
//Will be mapped to a table in database and it's called volunteers
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;
    @Column (nullable = false)
    private String role;

    @Column (nullable = false)
    private int hours;

    @Column (nullable = false)
    private String type;

    public Volunteer() {}

    public Volunteer(String username, String password, String email, String role, String type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.type = type;
        this.hours = 0;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public int getHours() {
        return hours;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


}
