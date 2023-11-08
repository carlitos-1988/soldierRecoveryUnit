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
    private String firstName;
    private String lastName;
    private MilitaryGrades caretakerGrade;
    private int phoneNumber;
    @OneToMany(mappedBy = "assignedCareTaker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientModel> caretakerPatients;
    private boolean isCareTaker;
    private boolean isAdmin;

    public CaretakerModel() {
    }

    public CaretakerModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.caretakerPatients = new ArrayList<>();
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
}
