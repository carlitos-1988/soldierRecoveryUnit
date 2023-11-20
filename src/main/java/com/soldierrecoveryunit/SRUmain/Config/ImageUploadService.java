package com.soldierrecoveryunit.SRUmain.Config;

import com.soldierrecoveryunit.SRUmain.models.ImageModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.ImageRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

@Service
public class ImageUploadService {

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    ImageRepo imageRepo;

    public void uploadProfileImage(MultipartFile file, Principal p) throws IOException {

        if(p != null){
            String userName = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(userName);
            ImageModel newImage = new ImageModel();
            newImage.setPatientModel(loggedInUser);
            newImage.setImageBytes(file.getBytes());
            newImage.setImageName(loggedInUser.getLastName() + "_" + LocalDate.now());

            imageRepo.save(newImage);
            patientRepo.save(loggedInUser);
        }

    }
}
