package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PatientRepo extends JpaRepository<PatientModel, Long> {

    public PatientModel findByUsername(String username);

    public ArrayList<PatientModel> findAll();
}