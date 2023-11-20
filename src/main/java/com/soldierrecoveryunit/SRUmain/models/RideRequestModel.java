package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class RideRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;
    private String fullName;
    private String phoneNumber;
    private String contactEmail;
    private LocalDate dateOfAppointment;
    private String appointmentTime;
    private LocalDate dateSubmitted;
    private String location;
    private String squadLeader;
    private String squadLeaderPhoneNumber;
    private boolean isApproved;
    private String status;
    private String comments;
    private String approverName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sruLocationId")
    private SRULocationModel requestBelongsToSru;


    public RideRequestModel() {
    }

    public RideRequestModel(String fullName, String phoneNumber, String contactEmail, LocalDate dateOfAppointment, String location, String squadLeader, String squadLeaderPhoneNumber, String appointmentTime) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.contactEmail = contactEmail;
        this.dateOfAppointment = dateOfAppointment;
        this.location = location;
        this.squadLeader = squadLeader;
        this.squadLeaderPhoneNumber = squadLeaderPhoneNumber;
        this.isApproved =false;
        this.appointmentTime = appointmentTime;
        this.status = "PENDING";
        this.dateSubmitted = LocalDate.now();
    }

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public SRULocationModel getRequestBelongsToSru() {
        return this.requestBelongsToSru;
    }

    public void setRequestBelongsToSru(SRULocationModel requestBelongsToSru) {
        this.requestBelongsToSru = requestBelongsToSru;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LocalDate getDateOfAppointment() {
        return this.dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSquadLeader() {
        return this.squadLeader;
    }

    public void setSquadLeader(String squadLeader) {
        this.squadLeader = squadLeader;
    }

    public String getSquadLeaderPhoneNumber() {
        return this.squadLeaderPhoneNumber;
    }

    public void setSquadLeaderPhoneNumber(String squadLeaderPhoneNumber) {
        this.squadLeaderPhoneNumber = squadLeaderPhoneNumber;
    }

    public boolean isApproved() {
        return this.isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDate getDateSubmitted() {
        return this.dateSubmitted;
    }

    public String getAppointmentTime() {
        return this.appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }
}
