package com.example.redcross;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface  HourRequestRepository extends JpaRepository<HourRequest, Long> {
    void deleteByVolunteerId(long volunteerId);
    List<HourRequest> findByStatus(String status);
    List<HourRequest> findByVolunteerEmail(String email);
    List<HourRequest> findByApprovedById(Long approvedById);

}
