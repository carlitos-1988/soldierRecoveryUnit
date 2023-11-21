package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.models.EventModel;
import com.soldierrecoveryunit.SRUmain.models.ImageModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface ImageRepo extends JpaRepository<ImageModel, Long> {


    ArrayList<Optional> findByPatientModel(PatientModel patientModel);

    ArrayList<Optional> findByEventModel(EventModel eventModel);
}
