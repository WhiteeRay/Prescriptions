package com.example.prescriptions.event;

import com.example.prescriptions.dto.PrescriptionResponseDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class PrescriptionCreatedEvent extends ApplicationEvent {
    private final PrescriptionResponseDto prescription;

    public PrescriptionCreatedEvent(Object source, PrescriptionResponseDto prescription){
        super(source);
        this.prescription = prescription;
    }
}
