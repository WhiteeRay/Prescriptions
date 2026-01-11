package com.example.prescriptions.service;

import com.example.prescriptions.dto.PrescriptionRequestDto;
import com.example.prescriptions.dto.PrescriptionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDto create(PrescriptionRequestDto dto);

    PrescriptionResponseDto getById(Long id);

    List<PrescriptionResponseDto> getByPatientId(Long patientId);

    PrescriptionResponseDto update(Long id, PrescriptionRequestDto dto);

    void delete(Long id);

    List<PrescriptionResponseDto> getByDoctorName(String doctorName);

    List<PrescriptionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate);
}
