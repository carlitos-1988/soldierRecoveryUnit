package com.soldierrecoveryunit.SRUmain.Config;

import com.soldierrecoveryunit.SRUmain.models.PatientModel;
import com.soldierrecoveryunit.SRUmain.repos.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PatientRepo patientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        PatientModel patientModel = patientRepo.findByUsername(username);

        if(patientModel == null){
            throw new UsernameNotFoundException("Username not found");
        }

        UserDetails userDetails = User.withUsername(patientModel.getUsername())
                .password(patientModel.getPassword())
                .roles("USER")
                .build();

        return userDetails;
    }

}
