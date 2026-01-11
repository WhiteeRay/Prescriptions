package com.example.prescriptions.controller;

import com.example.prescriptions.dto.PrescriptionRequestDto;
import com.example.prescriptions.dto.PrescriptionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.prescriptions.service.PrescriptionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription Management", description = "APIs for managing medical prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @Operation(summary = "Create a new prescription",
            description = "Creates a new prescription for a patient. Patient must exist and validUntil must be after issueDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription created successfully",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<PrescriptionResponseDto> createPrescription(
            @Valid @RequestBody PrescriptionRequestDto requestDto) {
        PrescriptionResponseDto responseDto = prescriptionService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID",
            description = "Retrieves a prescription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription found",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(
            @Parameter(description = "Prescription ID") @PathVariable Long id) {
        PrescriptionResponseDto responseDto = prescriptionService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all prescriptions for a patient",
            description = "Retrieves all prescriptions for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescriptions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class)))
    })
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prescription",
            description = "Updates an existing prescription. Patient must exist and validUntil must be after issueDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription updated successfully",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Prescription or patient not found")
    })
    public ResponseEntity<PrescriptionResponseDto> updatePrescription(
            @Parameter(description = "Prescription ID") @PathVariable Long id,
            @Valid @RequestBody PrescriptionRequestDto requestDto) {
        PrescriptionResponseDto responseDto = prescriptionService.update(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription",
            description = "Deletes a prescription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<Void> deletePrescription(
            @Parameter(description = "Prescription ID") @PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/doctor")
    @Operation(summary = "Filter prescriptions by doctor",
            description = "Retrieves all prescriptions prescribed by a specific doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescriptions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class)))
    })
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByDoctor(
            @Parameter(description = "Doctor name") @RequestParam String doctorName) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getByDoctorName(doctorName);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/filter/date-range")
    @Operation(summary = "Filter prescriptions by date range",
            description = "Retrieves all prescriptions issued within a specific date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescriptions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PrescriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByDateRange(
            @Parameter(description = "Start date (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PrescriptionResponseDto> prescriptions = prescriptionService.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(prescriptions);
    }
}