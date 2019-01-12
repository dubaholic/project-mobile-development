package org.ap.edu.reportingapp.models;

public class Mededeling {
    private String schadeId;
    private String uitgevoerdDoorNaam;
    private long dateRepaired;
    private String mededelingText;
    private String uitgevoerdDoor;

    public String getSchadeId() {
        return schadeId;
    }

    public String getUitgevoerdDoorNaam() {
        return uitgevoerdDoorNaam;
    }

    public long getDateRepaired() {
        return dateRepaired;
    }

    public String getMededelingText() {
        return mededelingText;
    }

    public String getUitgevoerdDoor() {
        return uitgevoerdDoor;
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
