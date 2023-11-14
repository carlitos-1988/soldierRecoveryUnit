package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.dto.iMedicationTrackerDTO;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface PatientRepo extends JpaRepository<PatientModel, Long> {

    public PatientModel findByUsername(String username);

    public ArrayList<PatientModel> findAll();

    @Query(nativeQuery = true, value = "SELECT " +
            "    medication_model.medication_id as medicationId, " +
            "    medication_model.times_taken_per_day as timesNeededPerDay, " +
            "    medication_model.medication_name as medicationName, " +
            "    patient_model.first_name as ownerName, " +
            "    patient_model.last_name as ownerLastName, " +
            "    patient_model.patient_id as ownerId, " +
            "    medication_tracker_model.date_medication_set as dateTrackerSet, " +
            " medication_tracker_model.is_medication_taken as isMedicationTaken, " +
            " medication_tracker_model.medication_id as trackerId " +
            "FROM " +
            "    medication_model " +
            "INNER JOIN " +
            "    medication_tracker_model ON medication_model.medication_id = medication_tracker_model.belongs_to_medication_medication_id " +
            "INNER JOIN " +
            "    patient_model ON medication_model.patient_id = patient_model.patient_id " +
            "WHERE " +
            " medication_tracker_model.date_medication_set > current_date - interval '3 day'")
    public List<iMedicationTrackerDTO> getAllMedicalTrackerDTO();
}