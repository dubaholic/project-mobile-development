package org.ap.edu.reportingapp.models;

public class Schade {
    private String schadeId;
    private String email;
    private String verdieping;
    private String lokaal;
    private String categorie;
    private String fotoNaam;
    private int urgentie;
    private String opmerking;
    private long timeStamp;
    private boolean isAfgehandeld;

    public String getSchadeId() {
        return schadeId;
    }

    public String getEmail() {
        return email;
    }

    public String getVerdieping() {
        return verdieping;
    }

    public String getLokaal() {
        return lokaal;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getFotoNaam() {
        return fotoNaam;
    }

    public int getUrgentie() {
        return urgentie;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isAfgehandeld() {
        return isAfgehandeld;
    }

    public Schade(String schadeId, String email, String verdieping, String lokaal, String categorie, String fotoNaam, int urgentie, String opmerking, long timeStamp, boolean isAfgehandeld) {
        this.schadeId = schadeId;
        this.email = email;
        this.verdieping = verdieping;
        this.lokaal = lokaal;
        this.categorie = categorie;
        this.fotoNaam = fotoNaam;
        this.urgentie = urgentie;
        this.opmerking = opmerking;
        this.timeStamp = timeStamp;
        this.isAfgehandeld = isAfgehandeld;
    }

    public Schade() {
    }

    @Override
    public String toString() {
        return "Schade{" +
                "schadeId=" + schadeId +
                ", email='" + email + '\'' +
                ", verdieping='" + verdieping + '\'' +
                ", lokaal='" + lokaal + '\'' +
                ", categorie='" + categorie + '\'' +
                ", fotoNaam='" + fotoNaam + '\'' +
                ", urgentie=" + urgentie +
                ", opmerking='" + opmerking + '\'' +
                ", timeStamp=" + timeStamp +
                ", isAfgehandeld=" + isAfgehandeld +
                '}';
    }
}
