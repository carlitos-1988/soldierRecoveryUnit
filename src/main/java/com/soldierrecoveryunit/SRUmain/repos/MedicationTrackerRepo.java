package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.models.MedicationTrackerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationTrackerRepo extends JpaRepository<MedicationTrackerModel, Long> {
}
