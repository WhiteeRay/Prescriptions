package com.example.prescriptions.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.prescriptions.entity.Prescription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorName(String doctorName);

    @Query("SELECT p FROM Prescription p WHERE p.issueDate >= :startDate AND p.issueDate <= :endDate")
    List<Prescription> findByIssueDateBetween(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);


    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId AND p.doctorName = :doctorName")
    List<Prescription> findByPatientIdAndDoctorName(@Param("patientId") Long patientId,
                                                        @Param("doctorName") String doctorName);                                    
}
