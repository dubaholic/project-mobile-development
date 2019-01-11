package org.ap.edu.reportingapp.models;

/**
 * Created by Maarten on 19/11/2018.
 */

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

    public void setSchadeId(String schadeId) {
        this.schadeId = schadeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerdieping() {
        return verdieping;
    }

    public void setVerdieping(String verdieping) {
        this.verdieping = verdieping;
    }

    public String getLokaal() {
        return lokaal;
    }

    public void setLokaal(String lokaal) {
        this.lokaal = lokaal;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getFotoNaam() {
        return fotoNaam;
    }

    public void setFotoNaam(String fotoNaam) {
        this.fotoNaam = fotoNaam;
    }

    public int getUrgentie() {
        return urgentie;
    }

    public void setUrgentie(int urgentie) {
        this.urgentie = urgentie;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public void setOpmerking(String opmerking) {
        this.opmerking = opmerking;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isAfgehandeld() {
        return isAfgehandeld;
    }

    public void setAfgehandeld(boolean afgehandeld) {
        isAfgehandeld = afgehandeld;
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
