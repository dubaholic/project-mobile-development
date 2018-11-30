package org.ap.edu.reportingapp;

public class Scoren {
    private String email;
    private String schadeId;

    public Scoren(String email, String schadeId) {
        this.email = email;
        this.schadeId = schadeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchadeId() {
        return schadeId;
    }

    public void setSchadeId(String schadeId) {
        this.schadeId = schadeId;
    }

    @Override
    public String toString() {
        return "Scoren{" +
                "email='" + email + '\'' +
                ", schadeId='" + schadeId + '\'' +
                '}';
    }
}
