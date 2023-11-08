package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MedicationTrackerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long medicationId;

    private LocalDate dateMedicationTaken;
    private boolean isMedicationTaken;
    @ManyToOne
    private MedicationModel belongsToMedication;

}
