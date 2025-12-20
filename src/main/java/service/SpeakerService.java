package com.example.demo.service;

import com.example.demo.dto.SpeakerDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Speaker;
import com.example.demo.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final ModelMapper modelMapper;

    public SpeakerDTO createSpeaker(SpeakerDTO speakerDTO) {
        if (speakerRepository.existsByEmail(speakerDTO.getEmail())) {
            throw new IllegalArgumentException("Speaker with this email already exists");
        }

        Speaker speaker = modelMapper.map(speakerDTO, Speaker.class);
        Speaker savedSpeaker = speakerRepository.save(speaker);
        return modelMapper.map(savedSpeaker, SpeakerDTO.class);
    }

    public SpeakerDTO getSpeakerById(Long id) {
        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker not found"));
        return modelMapper.map(speaker, SpeakerDTO.class);
    }

    public List<SpeakerDTO> getAllSpeakers() {
        return speakerRepository.findAll()
                .stream()
                .map(speaker -> modelMapper.map(speaker, SpeakerDTO.class))
                .collect(Collectors.toList());
    }

    public SpeakerDTO updateSpeaker(Long id, SpeakerDTO speakerDTO) {
        Speaker existingSpeaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker not found"));

        modelMapper.map(speakerDTO, existingSpeaker);
        existingSpeaker.setId(id);
        Speaker updatedSpeaker = speakerRepository.save(existingSpeaker);

        return modelMapper.map(updatedSpeaker, SpeakerDTO.class);
    }

    public void deleteSpeaker(Long id) {
        if (!speakerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Speaker not found");
        }
        speakerRepository.deleteById(id);
    }
}