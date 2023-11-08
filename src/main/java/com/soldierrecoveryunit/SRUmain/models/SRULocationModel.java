package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SRULocationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long sruLocationId;
    private String sruName;
    private String mailingAddress;

    @OneToMany(mappedBy = "assignedSRULocation",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientModel> patientToSRU;

    public SRULocationModel(String sruName) {
        this.sruName = sruName;
        this.patientToSRU = new ArrayList<>();
    }

    public SRULocationModel() {
    }

    public String getSruName() {
        return sruName;
    }

    public void setSruName(String sruName) {
        this.sruName = sruName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<PatientModel> getPatientToSRU() {
        return this.patientToSRU;
    }

    public void setPatientToSRU(ArrayList<PatientModel> patientToSRU) {
        this.patientToSRU = patientToSRU;
    }
    public void setPatientToSRU(PatientModel patientModel){
        this.patientToSRU.add(patientModel);
    }
}
