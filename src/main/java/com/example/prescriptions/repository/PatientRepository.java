package com.example.prescriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.prescriptions.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

}