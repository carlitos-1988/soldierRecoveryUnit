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

    @OneToMany(mappedBy = "belongsToSru", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventModel> locationEvents;



    public SRULocationModel(String sruName) {
        this.sruName = sruName;
        this.patientToSRU = new ArrayList<>();
    }

    public Long getSruLocationId() {
        return this.sruLocationId;
    }

    public void setSruLocationId(Long sruLocationId) {
        this.sruLocationId = sruLocationId;
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

    public List<EventModel> getLocationEvents() {
        return this.locationEvents;
    }

    public void setNewEvent(EventModel newEvent) {
        this.locationEvents.add(newEvent);
    }
}
