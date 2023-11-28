package com.soldierrecoveryunit.SRUmain.models;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CaretakerModel implements UserDetailsService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caretakerId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String caretakerGrade;
   private SRULocationModel assignedSru;
   private String password;
    private boolean hasProfileImage;
    private boolean isMedicalCareTaker;
    private boolean isAdmin;
    private boolean isDriver;


    @OneToMany(mappedBy = "assignedCareTaker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientModel> caretakerPatients;

    public CaretakerModel() {
    }

    public CaretakerModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.caretakerPatients = new ArrayList<>();
    }

    public Long getCaretakerId() {
        return this.caretakerId;
    }

    public void setCaretakerId(Long caretakerId) {
        this.caretakerId = caretakerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCaretakerGrade() {
        return this.caretakerGrade;
    }

    public void setCaretakerGrade(String caretakerGrade) {
        this.caretakerGrade = caretakerGrade;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHasProfileImage() {
        return this.hasProfileImage;
    }

    public void setHasProfileImage(boolean hasProfileImage) {
        this.hasProfileImage = hasProfileImage;
    }

    public boolean isMedicalCareTaker() {
        return this.isMedicalCareTaker;
    }

    public void setMedicalCareTaker(boolean medicalCareTaker) {
        isMedicalCareTaker = medicalCareTaker;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isDriver() {
        return this.isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void setPatientToCaretaker(PatientModel patientModel){
        this.caretakerPatients.add(patientModel);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SRULocationModel getAssignedSru() {
        return this.assignedSru;
    }

    public void setAssignedSru(SRULocationModel assignedSru) {
        this.assignedSru = assignedSru;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PatientModel> getCaretakerPatients() {
        return caretakerPatients;
    }

    public void setCaretakerPatients(PatientModel newPatient) {
        this.caretakerPatients.add(newPatient);
    }
}
