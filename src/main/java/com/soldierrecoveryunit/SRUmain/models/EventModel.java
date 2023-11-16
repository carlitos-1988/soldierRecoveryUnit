package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EventModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long eventId;

    private String eventName;
    private String description;
    private LocalDate dateOfEvent;
    private String organizer;
    private String organizationHostingEvent;
    private String contactNumber;
    private String contactEmail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sruLocationId")
    private SRULocationModel belongsToSru;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "events_to_patients",
            joinColumns = {@JoinColumn(name = "eventId")},
            inverseJoinColumns = {@JoinColumn(name = "patientId")}
    )
    private Set<PatientModel> eventAttendees = new HashSet<>();

    public EventModel() {
    }

    public EventModel(String eventName, String description, LocalDate dateOfEvent, String organizer, String contactNumber, String contactEmail, String organizationHostingEvent) {
        this.eventName = eventName;
        this.description = description;
        this.dateOfEvent = dateOfEvent;
        this.organizer = organizer;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.organizationHostingEvent = organizationHostingEvent;
        this.eventAttendees = new HashSet<>();
    }

    public SRULocationModel getBelongsToSru() {
        return this.belongsToSru;
    }

    public void setBelongsToSru(SRULocationModel belongsToSru) {
        this.belongsToSru = belongsToSru;
    }

    public Set<PatientModel> getEventAttendees() {
        return this.eventAttendees;
    }

    public void setEventAttendees(PatientModel eventAttendees) {
        this.eventAttendees.add(eventAttendees);
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfEvent() {
        return this.dateOfEvent;
    }

    public void setDateOfEvent(LocalDate dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public long getEventId() {
        return this.eventId;
    }

    public String getOrganizationHostingEvent() {
        return this.organizationHostingEvent;
    }

    public void setOrganizationHostingEvent(String organizationHostingEvent) {
        this.organizationHostingEvent = organizationHostingEvent;
    }
}
