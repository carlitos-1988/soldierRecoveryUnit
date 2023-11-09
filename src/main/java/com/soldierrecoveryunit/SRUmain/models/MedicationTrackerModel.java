package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MedicationTrackerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long medicationId;
    private String medicationName;
    private LocalDate dateMedicationSet;
    private LocalDate dateMedicationTaken;
    private boolean isMedicationTaken;
    @ManyToOne
    private MedicationModel belongsToMedication;

    public MedicationTrackerModel() {
    }

    public MedicationTrackerModel(String medicationName) {
        this.medicationName = medicationName;
        this.isMedicationTaken= false;
        this.dateMedicationSet = LocalDate.now();
    }

    public LocalDate getDateMedicationTaken() {
        return dateMedicationTaken;
    }

    public void setDateMedicationTaken(LocalDate dateMedicationTaken) {
        this.dateMedicationTaken = dateMedicationTaken;
    }

    public boolean isMedicationTaken() {
        return isMedicationTaken;
    }

    public void setMedicationTaken(boolean medicationTaken) {
        isMedicationTaken = medicationTaken;
    }

    public MedicationModel getBelongsToMedication() {
        return belongsToMedication;
    }

    public void setBelongsToMedication(MedicationModel belongsToMedication) {
        this.belongsToMedication = belongsToMedication;
    }
}
