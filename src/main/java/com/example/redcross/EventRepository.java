package com.example.redcross;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EventRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByType(String type);
}
