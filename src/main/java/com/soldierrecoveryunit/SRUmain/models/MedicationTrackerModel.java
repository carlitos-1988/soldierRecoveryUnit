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
        return this.dateMedicationTaken;
    }

    public void setDateMedicationTaken(LocalDate dateMedicationTaken) {
        this.dateMedicationTaken = dateMedicationTaken;
    }

    public boolean isMedicationTaken() {
        return this.isMedicationTaken;
    }

    public LocalDate getDateMedicationSet() {
        return this.dateMedicationSet;
    }

    public void setDateMedicationSet(LocalDate dateMedicationSet) {
        this.dateMedicationSet = dateMedicationSet;
    }

    public void setMedicationTaken(boolean medicationTaken) {
        isMedicationTaken = medicationTaken;
    }

    public MedicationModel getBelongsToMedication() {
        return this.belongsToMedication;
    }

    public void setBelongsToMedication(MedicationModel belongsToMedication) {
        this.belongsToMedication = belongsToMedication;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public Long getMedicationId() {
        return medicationId;
    }
}
