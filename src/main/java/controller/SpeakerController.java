package com.example.demo.controller;

import com.example.demo.dto.SpeakerDTO;
import com.example.demo.service.SpeakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
public class SpeakerController {
    private final SpeakerService speakerService;

    @PostMapping
    public ResponseEntity<SpeakerDTO> createSpeaker(@Valid @RequestBody SpeakerDTO speakerDTO) {
        SpeakerDTO createdSpeaker = speakerService.createSpeaker(speakerDTO);
        return new ResponseEntity<>(createdSpeaker, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeakerDTO> getSpeaker(@PathVariable Long id) {
        SpeakerDTO speaker = speakerService.getSpeakerById(id);
        return ResponseEntity.ok(speaker);
    }

    @GetMapping
    public ResponseEntity<List<SpeakerDTO>> getAllSpeakers() {
        List<SpeakerDTO> speakers = speakerService.getAllSpeakers();
        return ResponseEntity.ok(speakers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpeakerDTO> updateSpeaker(
            @PathVariable Long id,
            @Valid @RequestBody SpeakerDTO speakerDTO) {
        SpeakerDTO updatedSpeaker = speakerService.updateSpeaker(id, speakerDTO);
        return ResponseEntity.ok(updatedSpeaker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }
}