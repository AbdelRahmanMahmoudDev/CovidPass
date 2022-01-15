package com.example.covidpassproject;

public class Scanner {
    private String Email;
    private String Password;
    private String Job;

    public Scanner() {}

    public Scanner(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }
}
