package org.ap.edu.reportingapp.models;

import java.util.Date;

/**
 * Created by Maarten on 14/12/2018.
 */

public class Mededeling {
    private String schadeId;
    private String uitgevoerdDoorNaam;
    private long dateRepaired;
    private String mededelingText;
    private String uitgevoerdDoor;

    public String getSchadeId() {
        return schadeId;
    }

    public void setSchadeId(String schadeId) {
        this.schadeId = schadeId;
    }

    public String getUitgevoerdDoorNaam() {
        return uitgevoerdDoorNaam;
    }

    public void setUitgevoerdDoorNaam(String uitgevoerdDoorNaam) {
        this.uitgevoerdDoorNaam = uitgevoerdDoorNaam;
    }

    public long getDateRepaired() {
        return dateRepaired;
    }

    public void setDateRepaired(long dateRepaired) {
        this.dateRepaired = dateRepaired;
    }

    public String getMededelingText() {
        return mededelingText;
    }

    public void setMededelingText(String mededelingText) {
        this.mededelingText = mededelingText;
    }

    public String getUitgevoerdDoor() {
        return uitgevoerdDoor;
    }

    public void setUitgevoerdDoor(String uitgevoerdDoor) {
        this.uitgevoerdDoor = uitgevoerdDoor;
    }

    public Mededeling(String schadeId, String uitgevoerdDoorNaam, long dateRepaired, String mededelingText, String uitgevoerdDoor) {
        this.schadeId = schadeId;
        this.uitgevoerdDoorNaam = uitgevoerdDoorNaam;
        this.dateRepaired = dateRepaired;
        this.mededelingText = mededelingText;
        this.uitgevoerdDoor = uitgevoerdDoor;
    }

    public Mededeling() {
    }

    @Override
    public String toString() {
        return "Mededeling{" +
                "schadeId='" + schadeId + '\'' +
                ", uitgevoerdDoorNaam='" + uitgevoerdDoorNaam + '\'' +
                ", dateRepaired=" + dateRepaired +
                ", mededelingText='" + mededelingText + '\'' +
                ", uitgevoerdDoor='" + uitgevoerdDoor + '\'' +
                '}';
    }
}
