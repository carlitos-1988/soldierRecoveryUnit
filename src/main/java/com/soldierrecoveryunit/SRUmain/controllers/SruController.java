package com.soldierrecoveryunit.SRUmain.controllers;


import com.soldierrecoveryunit.SRUmain.Config.ImageUploadService;
import com.soldierrecoveryunit.SRUmain.models.*;
import com.soldierrecoveryunit.SRUmain.repos.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("sru")
public class SruController {

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    SRULocationRepo sruLocationRepo;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    ImageRepo imageRepo;
    @Autowired
    RideRequestRepo rideRequestRepo;
    @Autowired
    ImageUploadService imageUploadService;


    @GetMapping("/mainPage")
    public String getSruPage(Principal p, Model m) {

        String username = p.getName();
        PatientModel loggedinPatient = patientRepo.findByUsername(username);
        m.addAttribute("user", loggedinPatient.getEmail());

        return "sruMainPage.html";
    }

    @GetMapping("/sruEvents")
    @Transactional
    public String getSruEvents(Principal p, Model m) {

        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);
            List<EventModel> sruEvents = new ArrayList<>(eventRepo.findAll());

            Set<Long> rsvpEventIds = loggedinPatient.getMySruEvents().stream().map(EventModel :: getEventId).collect(Collectors.toSet());

            m.addAttribute("rsvpEventIds", rsvpEventIds);
            m.addAttribute("sruEvents", sruEvents);
        }

        return "sru-events-page.html";
    }



    @GetMapping("rideRequest")
    public String requestRide(Principal p, Model m) {
        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);

            Optional<SRULocationModel> assignedSru = sruLocationRepo.findById(loggedinPatient.getAssignedSRULocation().getSruLocationId());
            LocalDate loadedDate = LocalDate.now();


            assignedSru.ifPresent(sru -> {
                List<RideRequestModel> sruRides = rideRequestRepo.findAllByRequestBelongsToSru(sru);
                List<RideRequestModel> modifiedRides = new ArrayList<>();
                for (RideRequestModel request: sruRides) {
                    if (loadedDate.isBefore(request.getDateOfAppointment()) ){
                        modifiedRides.add(request);
                    }
                }
                m.addAttribute("sruRides", modifiedRides);
            });
        }

        return "request-ride.html";
    }

    @GetMapping("rideAdmin")
    public String adminRideRequests(Principal p, Model m) {
        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);

            Optional<SRULocationModel> assignedSru = sruLocationRepo.findById(loggedinPatient.getAssignedSRULocation().getSruLocationId());
            LocalDate loadedDate = LocalDate.now();

            assignedSru.ifPresent(sru -> {
                List<RideRequestModel> sruRides = rideRequestRepo.findAllByRequestBelongsToSru(sru);
                List<RideRequestModel> modifiedRides = new ArrayList<>();

                for (RideRequestModel request: sruRides) {
                    if (loadedDate.isBefore(request.getDateOfAppointment()) ){
                        modifiedRides.add(request);
                    }
                }
                m.addAttribute("sruRides", modifiedRides);
            });
        }

        return "sru-rides-admin.html";
    }


    @GetMapping("/addSruEvent")
    public String addSruEvent(Principal p, Model m) {
        return "add-sru-event";
    }

    @GetMapping("/resources")
    public String getResourcesPage(Principal p, Model m) {
        return "sru-resources-page.html";
    }

    @PostMapping("/saveEvent")
    public RedirectView saveEvent(Principal p, String eventName, String description, LocalDate dateOfEvent, String organizer, String contactNumber, String contactEmail, String organization, @RequestParam("file")MultipartFile file) throws IOException {

        if (p != null) {
            //System.out.println(eventName + " " + description + " " + dateOfEvent + " " + organizer + " " + contactNumber + " " + contactEmail);
            String username = p.getName();

            PatientModel loggedinPatient = patientRepo.findByUsername(username);

            EventModel newEvent = new EventModel(eventName, description, dateOfEvent, organizer, contactNumber, contactEmail, organization);
            newEvent.setBelongsToSru(loggedinPatient.getAssignedSRULocation());
            newEvent.setHasImage(true);

            SRULocationModel saveToSru = loggedinPatient.getAssignedSRULocation();
            saveToSru.setNewEvent(newEvent);

            eventRepo.save(newEvent);
            sruLocationRepo.save(saveToSru);

            imageUploadService.uploadSruEventImage(file,newEvent);

        }
        return new RedirectView("/sru/sruEvents");
    }


    @PostMapping("/saveRequestRide")
    public RedirectView saveRideRequest(Principal p, Model m, String fullName, String phoneNumber, String contactEmail, LocalDate dateOfAppointment, String location, String squadLeader, String squadLeaderPhoneNumber, String appointmentTime) {

        //System.out.println(fullName + " " + phoneNumber + " "+ contactEmail+" "+dateOfAppointment+" "+location+" "+ squadLeader+" "+ squadLeaderPhoneNumber);
        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);
            SRULocationModel assignedSruForUser = loggedinPatient.getAssignedSRULocation();
            RideRequestModel newRequest = new RideRequestModel(fullName, phoneNumber, contactEmail, dateOfAppointment, location, squadLeader, phoneNumber, appointmentTime);

            newRequest.setRequestBelongsToSru(assignedSruForUser);
            assignedSruForUser.setRideRequested(newRequest);

            rideRequestRepo.save(newRequest);
            sruLocationRepo.save(assignedSruForUser);

        }

        return new RedirectView("/sru/rideRequest");
    }

    @PostMapping("/updateRideRequest")
    public RedirectView updateRideRequest(Principal p, String rideRequestId, String newStatus, String comment) {
        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);
            System.out.println(rideRequestId + " : request id, " + newStatus + " : new status"+ comment + ": comment");
            Optional<RideRequestModel> requestToUpdate =  rideRequestRepo.findById(Long.parseLong(rideRequestId));

            requestToUpdate.ifPresent(rideRequestModel -> {
                rideRequestModel.setStatus(newStatus);
                rideRequestModel.setComments(comment);
                if (comment == "APPROVED"){
                    rideRequestModel.setApproved(true);
                }
                rideRequestRepo.save(rideRequestModel);
            });

        }
        return new RedirectView("/sru/rideAdmin");
    }

    @PostMapping("/addRsvpEvent")
    @Transactional
    public RedirectView addEventToMyEvents(Principal p, String eventId){

        if(p != null){
            String username = p.getName();
            PatientModel loggedInUser = patientRepo.findByUsername(username);
            Optional<EventModel> selectedEvent = eventRepo.findById(Long.parseLong(eventId));

            selectedEvent.ifPresent(eventModel -> {
                loggedInUser.addEvent(eventModel);
                eventModel.setEventAttendee(loggedInUser);

                patientRepo.save(loggedInUser);
                eventRepo.save(eventModel);
            });
        }
        return new RedirectView("/sru/sruEvents");
    }


    @GetMapping("image")
    @Transactional
    public ResponseEntity<byte[]> getEventImage(@RequestParam Long eventId) {
        try {
            Optional<EventModel> eventModelOptional = eventRepo.findById(eventId);
            if (eventModelOptional.isPresent()) {
                EventModel event = eventModelOptional.get();
                ImageModel image = event.getEventImage(); // Assuming eventModel has a reference to image directly

                if (image != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
                    return new ResponseEntity<>(image.getImageBytes(), headers, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
