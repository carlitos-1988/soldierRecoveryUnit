package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class MedicationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long medicationId;
    private String medicationName;
    private String doctorsName;
    private int dosageInMilligrams;
    private int medicationQuantity;
    private int timesTakenPerDay;
    private LocalDate expectedRefillDate;
    private LocalDate dateMedicationPrescribed;

    @OneToMany(mappedBy = "belongsToMedication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicationTrackerModel> dailyTakenMedication;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private PatientModel patientToMedication;

    public MedicationModel() {
    }

    public MedicationModel(String medicationName, String doctorsName, int dosageInMilligrams, int medicationQuantity, int timesTakenPerDay, LocalDate dateMedicationPrescribed) {
        this.medicationName = medicationName;
        this.doctorsName = doctorsName;
        this.dosageInMilligrams = dosageInMilligrams;
        this.medicationQuantity = medicationQuantity;
        this.timesTakenPerDay = timesTakenPerDay;
        this.dateMedicationPrescribed = dateMedicationPrescribed;
    }

    public PatientModel getPatientToMedication() {
        return this.patientToMedication;
    }

    public void setPatientToMedication(PatientModel patientToMedication) {
        this.patientToMedication = patientToMedication;
    }

    public void calculateRefillDate(){
        int daysOfMedication = this.medicationQuantity/this.timesTakenPerDay;
        LocalDate refillDate = this.dateMedicationPrescribed.plusDays(daysOfMedication);
        this.expectedRefillDate = refillDate;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDoctorsName() {
        return this.doctorsName;
    }

    public void setDoctorsName(String doctorsName) {
        this.doctorsName = doctorsName;
    }

    public int getDosageInMilligrams() {
        return this.dosageInMilligrams;
    }

    public void setDosageInMilligrams(int dosageInMilligrams) {
        this.dosageInMilligrams = dosageInMilligrams;
    }

    public int getMedicationQuantity() {
        return this.medicationQuantity;
    }

    public void setMedicationQuantity(int medicationQuantity) {
        this.medicationQuantity = medicationQuantity;
    }

    public int getTimesTakenPerDay() {
        return this.timesTakenPerDay;
    }

    public void setTimesTakenPerDay(int timesTakenPerDay) {
        this.timesTakenPerDay = timesTakenPerDay;
    }

    public LocalDate getExpectedRefillDate() {
        return this.expectedRefillDate;
    }

    public void setExpectedRefillDate(LocalDate expectedRefillDate) {
        this.expectedRefillDate = expectedRefillDate;
    }

    public LocalDate getDateMedicationPrescribed() {
        return this.dateMedicationPrescribed;
    }

    public void setDateMedicationPrescribed(LocalDate dateMedicationPrescribed) {
        this.dateMedicationPrescribed = dateMedicationPrescribed;
    }

    public List<MedicationTrackerModel> getDailyTakenMedication() {
        return this.dailyTakenMedication;
    }

    public void setDailyTakenMedication(MedicationTrackerModel takenMedication) {
        this.dailyTakenMedication.add(takenMedication);
    }

    @Override
    public String toString() {
        return "MedicationModel{" +
                "medicationId=" + this.medicationId +
                ", medicationName='" + this.medicationName + '\'' +
                ", doctorsName='" + this.doctorsName + '\'' +
                ", dosageInMilligrams=" + this.dosageInMilligrams +
                ", medicationQuantity=" + this.medicationQuantity +
                ", timesTakenPerDay=" + this.timesTakenPerDay +
                '}';
    }
}
