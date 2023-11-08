package com.soldierrecoveryunit.SRUmain.controllers;

import com.soldierrecoveryunit.SRUmain.models.MedicationModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.MedicationRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("patient")
public class PatientController {

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    MedicationRepo medicationRepo;

    @PostMapping("/addMedication")
    public String addNewMedication(Principal p, String medication, String dosage, String doctor, String quantity, String times, String date){

        if (p != null){
            String username = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(username);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.println(MessageFormat.format("{0}: Medication, {1}: dosage, {2}: Doctor, {3}: Quantity, {4}: times, {5}: date",medication,dosage,doctor,quantity,times,date));
            int medicationQuantity = Integer.parseInt(quantity);
            int timesTakenPerDay = Integer.parseInt(times);
            int dosageConverted = Integer.parseInt(dosage);
            LocalDate dateMedsIssued = LocalDate.parse(date,formatter);

            MedicationModel newMedication = new MedicationModel(medication,doctor,dosageConverted,medicationQuantity,timesTakenPerDay,dateMedsIssued);
            newMedication.setPatientToMedication(loggedInUser);
            newMedication.calculateRefillDate();
            loggedInUser.setMyMedication(newMedication);

            medicationRepo.save(newMedication);
            patientRepo.save(loggedInUser);

            return "redirect:/myPage";
        }

        return "redirect:/myPage";
    }

    @PostMapping("deleteMedication")
    public RedirectView deleteMedication(Principal p, String medicationId){



        Long medicationIDModified = Long.parseLong(medicationId);
        MedicationModel medicationModel = medicationRepo.findByMedicationId(medicationIDModified);
        System.out.println(medicationModel.toString());

        if (p != null) {
            String username = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(username);
            loggedInUser.removeMedication(medicationModel);
            patientRepo.save(loggedInUser);
            medicationRepo.delete(medicationModel);
        }

        return new RedirectView("/myPage");
    }

    @PostMapping("editMedication")
    public RedirectView editMedication(Principal p, String dosage, String quantity, String times, String medicationId){
        int newDosage = Integer.parseInt(dosage);
        int newQuantity = Integer.parseInt(quantity);
        int newTimesTaken = Integer.parseInt(times);
        Long adjustedMedicationID = Long.parseLong(medicationId);

        if (p != null) {
            String username = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(username);
            MedicationModel fetchedMedication = medicationRepo.findByMedicationId(adjustedMedicationID);
            fetchedMedication.setDosageInMilligrams(newDosage);
            fetchedMedication.setMedicationQuantity(newQuantity);
            fetchedMedication.setTimesTakenPerDay(newTimesTaken);
            fetchedMedication.calculateRefillDate();
            medicationRepo.save(fetchedMedication);

        }


        return new RedirectView("/myPage");
    }
}