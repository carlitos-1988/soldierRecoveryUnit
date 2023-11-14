package com.soldierrecoveryunit.SRUmain.dto;

import java.time.LocalDate;

public interface iMedicationTrackerDTO {

    public int  getMedicationId();
    public int getTimesNeededPerDay();
    public String getMedicationName();
    public String getOwnerName();
    public String getOwnerLastName();
    public int getOwnerId();
    public LocalDate getDateTrackerSet();
    public boolean getIsMedicationTaken();
    public String getTrackerId();


}
