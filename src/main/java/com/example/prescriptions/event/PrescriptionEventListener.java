package com.example.prescriptions.event;

import com.example.prescriptions.dto.PrescriptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PrescriptionEventListener{
    @EventListener
    @Async

    public void handlePrescriptionCreated(PrescriptionCreatedEvent event){
        PrescriptionResponseDto prescription = event.getPrescription();

        log.info("PRESCRIPTION - NOTIFICATION");
        log.info("Prescription id: {}", prescription.getId());
        log.info("Patient id: {}", prescription.getPatientId());
        log.info("Doctor: {}", prescription.getDoctorName());
        log.info("Medication: {}", prescription.getMedication());
        log.info("Dosage: {}", prescription.getDosage());
        log.info("Valid from: {} and valid until: {}", prescription.getIssueDate(), prescription.getValidUntil());
    }
}

