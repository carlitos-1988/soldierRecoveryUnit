package com.soldierrecoveryunit.SRUmain.controllers;

import com.soldierrecoveryunit.SRUmain.Config.ImageUploadService;
import com.soldierrecoveryunit.SRUmain.models.ImageModel;
import com.soldierrecoveryunit.SRUmain.models.MedicationModel;
import com.soldierrecoveryunit.SRUmain.models.MedicationTrackerModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.ImageRepo;
import com.soldierrecoveryunit.SRUmain.repos.MedicationRepo;
import com.soldierrecoveryunit.SRUmain.repos.MedicationTrackerRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("patient")
public class PatientController {

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    MedicationRepo medicationRepo;
    @Autowired
    MedicationTrackerRepo medicationTrackerRepo;
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    ImageRepo imageRepo;



    @GetMapping("/uploadImage")
    public String showUploadForm(Model model, Principal p) {
        String userName = p.getName();
        PatientModel patientModel = patientRepo.findByUsername(userName);
        model.addAttribute("userId", patientModel.getPatientId());

        return "uploadForm.html";
    }

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

    @PostMapping("updatePatient")
    public RedirectView updatePatient(Principal p, String email, String room,String careTaker, String verified ){
        boolean confirmed = (verified == "true")?false: Boolean.parseBoolean(verified);
        System.out.println("email: "+ email + " Room: "+ room + " CareTaker: " + careTaker + " Verified: " + verified + " boolean: "+ confirmed);

        if (p != null && confirmed) {
            String username = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(username);
            loggedInUser.setEmail(email);
            loggedInUser.setRoomAssignment(room);
            patientRepo.save(loggedInUser);

        }
        return new RedirectView("/myPage");
    }

    @PostMapping("takeMedication")
    public RedirectView takeMedication(Principal p, String trackerId){
        LocalDate todaysDate =  LocalDate.now();
        Long medicationTrackerId = Long.parseLong(trackerId);
        Optional<MedicationTrackerModel> takenMeds = medicationTrackerRepo.findById(Long.parseLong(trackerId));

        takenMeds.ifPresent(medicationTrackerModel -> {
            System.out.println(medicationTrackerModel.getMedicationId() + " : tracker ID");
            medicationTrackerModel.setMedicationTaken(true);
            System.out.println(medicationTrackerModel.isMedicationTaken());
            medicationTrackerModel.setDateMedicationTaken(todaysDate);
            System.out.println(medicationTrackerModel.getDateMedicationSet());
            medicationTrackerRepo.save(medicationTrackerModel);
        });


        return  new RedirectView("/myPage");
    }

    @PostMapping("imageUpload")
    public RedirectView uploadImage(@RequestParam("file")MultipartFile file,Principal p){
        String userName = p.getName();
        PatientModel patientModel = patientRepo.findByUsername(userName);
        try{
            patientModel.setHasProfileImage(true);
            imageUploadService.uploadProfileImage(file,p);
            return new RedirectView("/myPage");

        }catch (IOException e){
            return new RedirectView("/myPage");
        }

    }

    @GetMapping("image")
    @Transactional
    public ResponseEntity<byte[]> getUserImage(Principal p){

        String username = p.getName();

        try{
            PatientModel loggedInPatient = patientRepo.findByUsername(username);
            if(loggedInPatient != null){
                ArrayList<Optional> imageOptional = imageRepo.findByPatientModel(loggedInPatient);
                if (!imageOptional.isEmpty()){
                    ImageModel image = (ImageModel) imageOptional.get(0).get();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
                    return new ResponseEntity<>(image.getImageBytes(), headers, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
