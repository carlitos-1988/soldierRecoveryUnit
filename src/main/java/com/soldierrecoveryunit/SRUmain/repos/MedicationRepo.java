package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.models.MedicationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepo extends JpaRepository<MedicationModel, Long> {


    MedicationModel findByMedicationId(long medicationId);
}
