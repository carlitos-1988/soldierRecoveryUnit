package com.soldierrecoveryunit.SRUmain.controllers;


import com.soldierrecoveryunit.SRUmain.models.EventModel;
import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.models.RideRequestModel;
import com.soldierrecoveryunit.SRUmain.models.SRULocationModel;
import com.soldierrecoveryunit.SRUmain.repos.EventRepo;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import com.soldierrecoveryunit.SRUmain.repos.RideRequestRepo;
import com.soldierrecoveryunit.SRUmain.repos.SRULocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    RideRequestRepo rideRequestRepo;


    @GetMapping("/mainPage")
    public String getSruPage(Principal p, Model m) {

        String username = p.getName();
        PatientModel loggedinPatient = patientRepo.findByUsername(username);
        m.addAttribute("user", loggedinPatient.getEmail());

        return "sruMainPage.html";
    }

    @GetMapping("/sruEvents")
    public String getSruEvents(Principal p, Model m) {

        if (p != null) {
            String username = p.getName();
            PatientModel loggedinPatient = patientRepo.findByUsername(username);
            List<EventModel> sruEvents = new ArrayList<>(eventRepo.findAll());

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
    public RedirectView saveEvent(Principal p, String eventName, String description, LocalDate dateOfEvent, String organizer, String contactNumber, String contactEmail, String organization) {

        if (p != null) {
            //System.out.println(eventName + " " + description + " " + dateOfEvent + " " + organizer + " " + contactNumber + " " + contactEmail);
            String username = p.getName();

            PatientModel loggedinPatient = patientRepo.findByUsername(username);

            EventModel newEvent = new EventModel(eventName, description, dateOfEvent, organizer, contactNumber, contactEmail, organization);
            newEvent.setBelongsToSru(loggedinPatient.getAssignedSRULocation());

            SRULocationModel saveToSru = loggedinPatient.getAssignedSRULocation();
            saveToSru.setNewEvent(newEvent);

            eventRepo.save(newEvent);
            sruLocationRepo.save(saveToSru);
            System.out.println("able to create a new event");

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
}
