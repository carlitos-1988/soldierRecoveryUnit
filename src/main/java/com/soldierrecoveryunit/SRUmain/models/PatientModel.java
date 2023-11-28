package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

@Entity
public class PatientModel implements UserDetailsService {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long patientId;
private String firstName;
private String lastName;
private String phoneNumber;
private String roomAssignment;

private MilitaryGrades patientMilitaryGrade;
private String email;
private String username;
private String password;
private boolean isDriver;
private boolean hasProfileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sru_location_id", referencedColumnName = "sruLocationId")
    @Fetch(FetchMode.JOIN) // Explicitly set the fetching strategy
private SRULocationModel assignedSRULocation;


@ManyToOne
@JoinColumn(name = "caretaker_id")
private CaretakerModel assignedCareTaker;


@OneToMany(mappedBy = "patientToMedication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
List<MedicationModel> myMedications;

@ManyToMany(mappedBy = "eventAttendees")
private Set<EventModel> mySruEvents = new HashSet<>();


    public PatientModel() {
    }

    public PatientModel(String firstName, String lastName, String phoneNumber, String roomAssignment, SRULocationModel assignedSRULocation, MilitaryGrades patientMilitaryGrade, CaretakerModel assignedCareTaker, String email, String username) {

    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.roomAssignment = roomAssignment;
    this.assignedSRULocation = assignedSRULocation;
    this.patientMilitaryGrade = patientMilitaryGrade;
    this.assignedCareTaker = assignedCareTaker;
    this.email = email;
    this.username = username;
    this.myMedications = new ArrayList<>();
    this.hasProfileImage = false;
}

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoomAssignment() {
        return this.roomAssignment;
    }

    public void setRoomAssignment(String roomAssignment) {
        this.roomAssignment = roomAssignment;
    }

    public MilitaryGrades getPatientMilitaryGrade() {
        return patientMilitaryGrade;
    }

    public void setPatientMilitaryGrade(MilitaryGrades patientMilitaryGrade) {
        this.patientMilitaryGrade = patientMilitaryGrade;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SRULocationModel getAssignedSRULocation() {
        return this.assignedSRULocation;
    }

    public void setAssignedSRULocation(SRULocationModel assignedSRULocation) {
        this.assignedSRULocation = assignedSRULocation;
    }

    public CaretakerModel getAssignedCareTaker() {
        return this.assignedCareTaker;
    }

    public void setAssignedCareTaker(CaretakerModel assignedCareTaker) {
        this.assignedCareTaker = assignedCareTaker;
    }

    public ArrayList<MedicationModel> getMyMedications() {
        return new ArrayList<MedicationModel>(this.myMedications);
    }

    public void setMyMedication(MedicationModel myMedication) {
        this.myMedications.add(myMedication);
    }

    public boolean removeMedication(MedicationModel medicationToRemove){
        if (medicationToRemove == null){
            return false;
        }
        boolean isRemoved = this.myMedications.remove(medicationToRemove);

        return isRemoved;
    }

    public LinkedList<ArrayList<MedicationTrackerModel>> patientReturnsNonTakenMedications(){
        LinkedList<ArrayList<MedicationTrackerModel>> returnListOfNonTakenMedications = new LinkedList<>();
        for(MedicationModel medication : this.myMedications){
            returnListOfNonTakenMedications.add(medication.getNonTakenMedication());
        }
        return returnListOfNonTakenMedications;
    }

    public boolean hasProfileImage() {
        return this.hasProfileImage;
    }

    public void setHasProfileImage(boolean hasProfileImage) {
        this.hasProfileImage = hasProfileImage;
    }

    public Set<EventModel> getMySruEvents() {
        return mySruEvents;
    }

    public void addEvent(EventModel sruEvent) {
        this.mySruEvents.add(sruEvent);
    }

    public boolean isDriver() {
        return this.isDriver;
    }

    public void setDriver(boolean driver) {
        this.isDriver = driver;
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", roomAssignment='" + roomAssignment + '\'' +
                ", patientMilitaryGrade=" + patientMilitaryGrade +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", assignedSRULocation=" + assignedSRULocation +
                ", assignedCareTaker=" + assignedCareTaker +
                ", myMedications=" + myMedications +
                '}';
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
