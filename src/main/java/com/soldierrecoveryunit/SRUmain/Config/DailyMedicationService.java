package com.soldierrecoveryunit.SRUmain.Config;

import com.soldierrecoveryunit.SRUmain.models.MedicationModel;
import com.soldierrecoveryunit.SRUmain.models.MedicationTrackerModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.MedicationRepo;
import com.soldierrecoveryunit.SRUmain.repos.MedicationTrackerRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;

@Service
public class DailyMedicationService {

    @Autowired
    MedicationRepo medicationRepo;
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    MedicationTrackerRepo medicationTrackerRepo;

    //@Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0/2 * * * * ?") //for development only
    @Transactional
    public void createDailyMedications(){

        ArrayList<PatientModel> allSruPatients = patientRepo.findAll();
        for (PatientModel patient : allSruPatients) {
            if (patient.getMyMedications().size() < 0 ){
                return;
            }
            ArrayList<MedicationModel> patientMedications = patient.getMyMedications();
            for(MedicationModel medication: patientMedications){
                String newTrackerName = medication.getMedicationName();
                MedicationTrackerModel dailyMedication = new MedicationTrackerModel(newTrackerName);
                medication.setDailyTakenMedication(dailyMedication);
                medicationTrackerRepo.save(dailyMedication);
                medicationRepo.save(medication);
            }

            patientRepo.save(patient);
        }
    }

    public static boolean checkLoggedInUserHelper(Principal p){
        if(p == null){
            return false;
        }

        return true;
    }


}