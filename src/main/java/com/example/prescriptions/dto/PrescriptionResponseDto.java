package com.example.prescriptions.dto;

import lombok.*;
import java.time.LocalDate;


@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDto{
    private Long id;
    private Long patientId;
    private String doctorName;
    private String medication; 
    private String dosage;
    private LocalDate issueDate;
    private LocalDate validUntil;
    private Boolean isExpired;

}