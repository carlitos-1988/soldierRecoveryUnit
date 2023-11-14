package com.soldierrecoveryunit.SRUmain.controllers;

import com.soldierrecoveryunit.SRUmain.dto.iMedicationTrackerDTO;
import com.soldierrecoveryunit.SRUmain.models.*;
import com.soldierrecoveryunit.SRUmain.repos.CaretakerRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import com.soldierrecoveryunit.SRUmain.repos.SRULocationRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    CaretakerRepo caretakerRepo;

    @Autowired
    SRULocationRepo sruLocationRepo;

    @Autowired
    HttpServletRequest request;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/")
    String getHomePage(){
        return "splash.html";
    }

    @GetMapping("/myPage")
    String getMyPage(Principal p, Model m ){
        if (p != null){
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);
            List<MedicationModel> patientMedications = new ArrayList<>(loggedinPatient.getMyMedications());
            List<iMedicationTrackerDTO> patientTrackers = patientRepo.getAllMedicalTrackerDTO();


            if (loggedinPatient!= null ){
                m.addAttribute("patientModel", loggedinPatient);
                m.addAttribute("patientMedications", patientMedications);
                List<iMedicationTrackerDTO> filteredModels = new ArrayList<>();

                for (iMedicationTrackerDTO tracker: patientTrackers ){
                    if (loggedinPatient.getPatientId() == tracker.getOwnerId() & tracker.getIsMedicationTaken() == false){
                        filteredModels.add(tracker);
                    }
                }
                m.addAttribute("myTrackers", filteredModels);
            }
            }

        return "myPage.html";
    }

    @GetMapping("/login")
    String getLogInPage(){
        return "splash.html";
    }

    @GetMapping("/signup")
    String getSignupPage() {
        return "signup.html";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new RedirectView("/");
    }

    @PostMapping("/signup")
    String signupUser(String email, String firstName, String lastName, String username, String roomAssignment, String phone, String sruLocation, MilitaryGrades gradeSelect, String password, Model model){
        //System.out.println(MessageFormat.format("email: {0}, firstName:{1}, lastName: {2}, username:{3}, roomAssignment: {4}, phone: {5}, SRU Location: {6}, military grade: {7}, password: {8} ", email,firstName, lastName, username,roomAssignment, phone, sruLocation, gradeSelect, password));

        // Check if the username is already taken
        PatientModel existingUser = patientRepo.findByUsername(username);
        if (existingUser != null) {
            // Username is taken, return to the signup page with an error message
            model.addAttribute("usernameError", "This username is already in use. Please create another.");
            return "signup"; // Return the signup view
        }

        CaretakerModel testCareTaker = new CaretakerModel("Test firstName", "Test LastName");
        caretakerRepo.save(testCareTaker);
        SRULocationModel sruLocationModel = new SRULocationModel("JBLM");
        sruLocationRepo.save(sruLocationModel);


        PatientModel newPatientModel = new PatientModel(firstName, lastName, phone,roomAssignment,sruLocationModel, gradeSelect, testCareTaker, email, username);
        patientRepo.save(newPatientModel);

        testCareTaker.setPatientToCaretaker(newPatientModel);
        sruLocationModel.setPatientToSRU(newPatientModel);


        String encodedPassword = passwordEncoder.encode(password);
        newPatientModel.setPassword(encodedPassword);
        patientRepo.save(newPatientModel);


        authWithHttpServletRequest(username, password);

        return "redirect:/";
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("Error while logging in.");
            e.printStackTrace();
        }
    }
}
