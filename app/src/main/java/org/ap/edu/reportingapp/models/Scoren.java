package org.ap.edu.reportingapp.models;

public class Scoren {
    private String email;
    private String schadeId;

    public String getEmail() {
        return email;
    }

    public String getSchadeId() {
        return schadeId;
    }

    public Scoren(String email, String schadeId) {
        this.email = email;
        this.schadeId = schadeId;
    }

    public Scoren() {
    }

    @Override
    public String toString() {
        return "Scoren{" +
                "email='" + email + '\'' +
                ", schadeId='" + schadeId + '\'' +
                '}';
    }
}
