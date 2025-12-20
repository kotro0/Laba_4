package com.example.demo.controller;

import com.example.demo.dto.RegistrationDTO;
import com.example.demo.model.Registration;
import com.example.demo.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Registration> registerParticipant(
            @RequestParam Long participantId,
            @RequestParam Long sessionId) {
        Registration registration = registrationService.registerParticipant(participantId, sessionId);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        Registration registration = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(registration);
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Registration>> getRegistrationsByParticipant(
            @PathVariable Long participantId) {
        List<Registration> registrations = registrationService.getRegistrationsByParticipant(participantId);
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Registration>> getRegistrationsBySession(
            @PathVariable Long sessionId) {
        List<Registration> registrations = registrationService.getRegistrationsBySession(sessionId);
        return ResponseEntity.ok(registrations);
    }
}