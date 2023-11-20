package com.soldierrecoveryunit.SRUmain.repos;

import com.soldierrecoveryunit.SRUmain.models.RideRequestModel;
import com.soldierrecoveryunit.SRUmain.models.SRULocationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRequestRepo extends JpaRepository<RideRequestModel,Long> {

    List<RideRequestModel> findAllByRequestBelongsToSru(SRULocationModel mySru);
}
