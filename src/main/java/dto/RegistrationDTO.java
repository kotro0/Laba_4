package com.example.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.example.demo.model.RegistrationStatus;

@Data
public class RegistrationDTO {
    private Long id;

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    private LocalDateTime registrationDate;
    private RegistrationStatus status;

    private String participantName;
    private String sessionTitle;
    private String eventName;
}