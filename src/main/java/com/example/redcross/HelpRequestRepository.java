package com.example.redcross;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    void deleteByVolunteerId(long volunteerId);
    List<HelpRequest> findByStatus(String status);
    List<HelpRequest> findByVolunteerEmail(String email);
    List<HelpRequest> findByStatusNot(String status);
}
