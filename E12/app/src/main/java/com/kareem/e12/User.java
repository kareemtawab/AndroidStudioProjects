package com.kareem.e12;

public class User {

    private String username;
    private String email;
    private String job;
    private String office;
    private int score;

    public User(String username, String email, String job, String office, int score) {
        this.username = username;
        this.email = email;
        this.job = job;
        this.office = office;
        this.score = score;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
