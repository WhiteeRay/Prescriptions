package com.example.prescriptions.service.impl;


import com.example.prescriptions.dto.PrescriptionRequestDto;
import com.example.prescriptions.dto.PrescriptionResponseDto;
import com.example.prescriptions.entity.Prescription;
import com.example.prescriptions.event.PrescriptionCreatedEvent;
import com.example.prescriptions.exception.NotFoundException;
import com.example.prescriptions.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.prescriptions.repository.PatientRepository;
import com.example.prescriptions.repository.PrescriptionRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.prescriptions.service.PrescriptionService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PrescriptionResponseDto create(PrescriptionRequestDto dto ){
        log.info("Creating prescription for patient Id: {}", dto.getPatientId());

        validatePatientExists(dto.getPatientId());
        validateDates(dto.getIssueDate(), dto.getValidUntil());

        Prescription prescription = Prescription.builder()
                .patientId(dto.getPatientId())
                .doctorName(dto.getDoctorName())
                .medication(dto.getMedication())
                .dosage(dto.getDosage())
                .issueDate(dto.getIssueDate())
                .validUntil(dto.getValidUntil())
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        log.info("Prescription created with Id: {}", savedPrescription.getId());

        PrescriptionResponseDto responseDto = toResponseDto(savedPrescription);

        eventPublisher.publishEvent(new PrescriptionCreatedEvent(this,responseDto));

        return responseDto;
    }


    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponseDto getById(Long id){
        log.info("Fetching prescription with ID: {}", id);

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Prescription", id));
        return toResponseDto(prescription);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getByPatientId(Long patientId){
        log.info("Fetching all prescription for patient Id: {}", patientId);

        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);

        return prescriptions.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public PrescriptionResponseDto update(Long id, PrescriptionRequestDto dto){
        log.info("Updating prescription with Id {}", id);

        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Prescription", id));

        validatePatientExists(dto.getPatientId());

        validateDates(dto.getIssueDate(),dto.getValidUntil());

        existingPrescription.setPatientId(dto.getPatientId());
        existingPrescription.setDoctorName(dto.getDoctorName());
        existingPrescription.setMedication(dto.getMedication());
        existingPrescription.setDosage(dto.getDosage());
        existingPrescription.setIssueDate(dto.getIssueDate());
        existingPrescription.setValidUntil(dto.getValidUntil());

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        log.info("Prescription updated with Id: {}", updatedPrescription.getId());

        return toResponseDto(updatedPrescription);
    }

    @Override
    public void delete(Long id){
        log.info("Deleting prescription with Id: {}", id);

        if (!prescriptionRepository.existsById(id)){
            throw new NotFoundException("Prescription", id);
        }
        prescriptionRepository.deleteById(id);
        log.info("Prescription deleted with Id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getByDoctorName(String doctorName) {
        log.info("Fetching prescriptions by doctor: {}", doctorName);
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorName(doctorName);


        return prescriptions.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching prescriptions between {} and {}", startDate, endDate);

        if (startDate.isAfter(endDate)){
            throw new ValidationException("Start date must be before or equal to end date");
        }
        List <Prescription> prescriptions = prescriptionRepository.findByIssueDateBetween(startDate,endDate);

        return prescriptions.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private void validatePatientExists(Long patientId){
        if (!patientRepository.existsById(patientId)){
            throw new NotFoundException("Patient", patientId);
        }
    }

    private void validateDates(LocalDate issueDate, LocalDate validUntil){
        if (validUntil.isBefore(issueDate)){
            throw new ValidationException(String.format("Valid until date %s cannot be before issue date %s", validUntil, issueDate)
            );
        }
    }

    private PrescriptionResponseDto toResponseDto(Prescription prescription){
        boolean isExpired = LocalDate.now().isAfter(prescription.getValidUntil());


        return PrescriptionResponseDto.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatientId())
                .doctorName(prescription.getDoctorName())
                .medication(prescription.getMedication())
                .dosage(prescription.getDosage())
                .issueDate(prescription.getIssueDate())
                .validUntil(prescription.getValidUntil())
                .isExpired(isExpired)
                .build();
    }
}
