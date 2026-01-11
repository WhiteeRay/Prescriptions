package com.example.prescriptions.service;

import com.example.prescriptions.dto.PrescriptionRequestDto;
import com.example.prescriptions.dto.PrescriptionResponseDto;
import com.example.prescriptions.entity.Patient;
import com.example.prescriptions.entity.Prescription;
import com.example.prescriptions.exception.NotFoundException;
import com.example.prescriptions.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import com.example.prescriptions.repository.PatientRepository;
import com.example.prescriptions.repository.PrescriptionRepository;
import com.example.prescriptions.service.impl.PrescriptionServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prescription Service Tests")
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private PrescriptionRequestDto validRequestDto;
    private Prescription prescription;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstName("Aknur")
                .lastName("Mazhitova")
                .build();

        validRequestDto = PrescriptionRequestDto.builder()
                .patientId(1L)
                .doctorName("Dr. Aiym")
                .medication("Amoxicillin")
                .dosage("500mg twice daily")
                .issueDate(LocalDate.now())
                .validUntil(LocalDate.now().plusDays(30))
                .build();

        prescription = Prescription.builder()
                .id(1L)
                .patientId(1L)
                .doctorName("Dr. Aiym")
                .medication("Amoxicillin")
                .dosage("500mg twice daily")
                .issueDate(LocalDate.now())
                .validUntil(LocalDate.now().plusDays(30))
                .build();
    }

    @Test
    @DisplayName("Should create prescription successfully when patient exists and dates are valid")
    void testCreatePrescription_Success() {

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);


        PrescriptionResponseDto result = prescriptionService.create(validRequestDto);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dr. Aiym", result.getDoctorName());
        assertEquals("Amoxicillin", result.getMedication());
        assertEquals("500mg twice daily", result.getDosage());

        verify(patientRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    @DisplayName("Should throw NotFoundException when patient does not exist")
    void testCreatePrescription_PatientNotFound() {

        when(patientRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> prescriptionService.create(validRequestDto)
        );

        assertTrue(exception.getMessage().contains("Patient"));
        assertTrue(exception.getMessage().contains("1"));

        verify(patientRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("Should throw ValidationException when validUntil is before issueDate")
    void testCreatePrescription_InvalidDates() {

        validRequestDto.setValidUntil(LocalDate.now().minusDays(1));
        when(patientRepository.existsById(1L)).thenReturn(true);


        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> prescriptionService.create(validRequestDto)
        );

        assertTrue(exception.getMessage().contains("Valid until date"));
        assertTrue(exception.getMessage().contains("cannot be before"));

        verify(prescriptionRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("Should retrieve prescription by ID successfully")
    void testGetById_Success() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));


        PrescriptionResponseDto result = prescriptionService.getById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dr. Aiym", result.getDoctorName());

        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw NotFoundException when prescription does not exist")
    void testGetById_NotFound() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> prescriptionService.getById(1L)
        );

        assertTrue(exception.getMessage().contains("Prescription"));
        assertTrue(exception.getMessage().contains("1"));

        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should retrieve all prescriptions for a patient")
    void testGetByPatientId_Success() {

        Prescription prescription2 = Prescription.builder()
                .id(2L)
                .patientId(1L)
                .doctorName("Dr. Sanzhar")
                .medication("Ibuprofen")
                .dosage("200mg as needed")
                .issueDate(LocalDate.now())
                .validUntil(LocalDate.now().plusDays(60))
                .build();

        List<Prescription> prescriptions = Arrays.asList(prescription, prescription2);
        when(prescriptionRepository.findByPatientId(1L)).thenReturn(prescriptions);


        List<PrescriptionResponseDto> result = prescriptionService.getByPatientId(1L);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dr. Aiym", result.get(0).getDoctorName());
        assertEquals("Dr. Sanzhar", result.get(1).getDoctorName());

        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }

    @Test
    @DisplayName("Should return empty list when patient has no prescriptions")
    void testGetByPatientId_EmptyList() {

        when(prescriptionRepository.findByPatientId(1L)).thenReturn(Arrays.asList());

        List<PrescriptionResponseDto> result = prescriptionService.getByPatientId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }

    @Test
    @DisplayName("Should update prescription successfully")
    void testUpdatePrescription_Success() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        validRequestDto.setMedication("Paracetamol");
        validRequestDto.setDosage("1000mg three times daily");


        PrescriptionResponseDto result = prescriptionService.update(1L, validRequestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent prescription")
    void testUpdatePrescription_NotFound() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> prescriptionService.update(1L, validRequestDto)
        );

        assertTrue(exception.getMessage().contains("Prescription"));

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete prescription successfully")
    void testDeletePrescription_Success() {

        when(prescriptionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(prescriptionRepository).deleteById(1L);


        prescriptionService.delete(1L);


        verify(prescriptionRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent prescription")
    void testDeletePrescription_NotFound() {

        when(prescriptionRepository.existsById(1L)).thenReturn(false);


        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> prescriptionService.delete(1L)
        );

        assertTrue(exception.getMessage().contains("Prescription"));

        verify(prescriptionRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should retrieve prescriptions by doctor name")
    void testGetByDoctorName_Success() {

        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionRepository.findByDoctorName("Dr. Aiym")).thenReturn(prescriptions);


        List<PrescriptionResponseDto> result = prescriptionService.getByDoctorName("Dr. Aiym");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Aiym", result.get(0).getDoctorName());

        verify(prescriptionRepository, times(1)).findByDoctorName("Dr. Aiym");
    }

    @Test
    @DisplayName("Should retrieve prescriptions by date range")
    void testGetByDateRange_Success() {

        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(10);
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionRepository.findByIssueDateBetween(startDate, endDate)).thenReturn(prescriptions);


        List<PrescriptionResponseDto> result = prescriptionService.getByDateRange(startDate, endDate);


        assertNotNull(result);
        assertEquals(1, result.size());

        verify(prescriptionRepository, times(1)).findByIssueDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should throw ValidationException when start date is after end date")
    void testGetByDateRange_InvalidRange() {

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(10);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> prescriptionService.getByDateRange(startDate, endDate)
        );

        assertTrue(exception.getMessage().contains("Start date"));
        assertTrue(exception.getMessage().contains("before or equal"));

        verify(prescriptionRepository, never()).findByIssueDateBetween(any(), any());
    }

    @Test
    @DisplayName("Should mark prescription as expired when validUntil is in the past")
    void testGetById_ExpiredPrescription() {

        prescription.setValidUntil(LocalDate.now().minusDays(1));
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));


        PrescriptionResponseDto result = prescriptionService.getById(1L);


        assertNotNull(result);
        assertTrue(result.getIsExpired());

        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should mark prescription as not expired when validUntil is in the future")
    void testGetById_ActivePrescription() {

        prescription.setValidUntil(LocalDate.now().plusDays(10));
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));


        PrescriptionResponseDto result = prescriptionService.getById(1L);


        assertNotNull(result);
        assertFalse(result.getIsExpired());

        verify(prescriptionRepository, times(1)).findById(1L);
    }
}