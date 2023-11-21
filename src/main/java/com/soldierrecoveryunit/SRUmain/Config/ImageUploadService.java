package com.soldierrecoveryunit.SRUmain.Config;

import com.soldierrecoveryunit.SRUmain.models.EventModel;
import com.soldierrecoveryunit.SRUmain.models.ImageModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.EventRepo;
import com.soldierrecoveryunit.SRUmain.repos.ImageRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ImageUploadService {

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    ImageRepo imageRepo;
    @Autowired
    EventRepo eventRepo;

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
    public void uploadSruEventImage(MultipartFile file, EventModel eventModel) throws IOException {
        if (eventModel != null) {
            Optional<EventModel> eventModelSearch = eventRepo.findById(eventModel.getEventId());
            eventModelSearch.ifPresent(foundEventModel -> {
                ImageModel newImage = new ImageModel();
                try {
                    newImage.setImageBytes(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                newImage.setImageName(foundEventModel.getEventName() + "_" + LocalDate.now());
                newImage.setEventModel(foundEventModel);

                foundEventModel.setEventImage(newImage);

                eventRepo.save(foundEventModel); // Save the EventModel after associating the ImageModel
                imageRepo.save(newImage); // Save the ImageModel after associating it with the EventModel
            });
        }
    }
}
