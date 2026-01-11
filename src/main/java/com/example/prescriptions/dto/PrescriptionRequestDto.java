package com.example.prescriptions.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class PrescriptionRequestDto {
    @NotNull(message = "Patient ID is required")
    private Long patientId;


    @NotBlank(message = "Doctor name is required")
    @Size(max = 100, message = "Doctor name must not exceed 100 characters.")
    private String doctorName;

    @NotBlank(message = "Medication is required.")
    @Size(max = 200, message = "Medication must not exceed 200 characters.")
    private String medication;

    @NotBlank(message = "Number of dosage is required")
    @Size(max = 100, message = "Dosage must not exceed 100 characters.")
    private String dosage;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future.")
    private LocalDate issueDate;

    @NotNull(message = "Valid until date is required")
    @PastOrPresent(message = "Valid until date must be in the future.")
    private LocalDate validUntil;
}
