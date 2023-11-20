package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

@Entity
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long imageId;
    String imageName;
    @Lob
    byte[] imageBytes;
    @ManyToOne
    @JoinColumn(name="patientId")
    PatientModel patientModel;

    @ManyToOne
    @JoinColumn(name = "eventId")
    EventModel eventModel;

    public ImageModel() {
    }

    public ImageModel(String imageName, byte[] imageBytes) {
        this.imageName = imageName;
        this.imageBytes = imageBytes;
    }

    public Long getImageId() {
        return this.imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageBytes() {
        return this.imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public PatientModel getPatientModel() {
        return this.patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
    }

    public EventModel getEventModel() {
        return this.eventModel;
    }

    public void setEventModel(EventModel eventModel) {
        this.eventModel = eventModel;
    }
}
