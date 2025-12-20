package com.example.demo.service;

import com.example.demo.dto.ParticipantDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Participant;
import com.example.demo.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ModelMapper modelMapper;

    public ParticipantDTO createParticipant(ParticipantDTO participantDTO) {
        if (participantRepository.existsByEmail(participantDTO.getEmail())) {
            throw new IllegalArgumentException("Participant with this email already exists");
        }

        Participant participant = modelMapper.map(participantDTO, Participant.class);
        Participant savedParticipant = participantRepository.save(participant);
        return modelMapper.map(savedParticipant, ParticipantDTO.class);
    }

    public ParticipantDTO getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        return modelMapper.map(participant, ParticipantDTO.class);
    }

    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(participant -> modelMapper.map(participant, ParticipantDTO.class))
                .collect(Collectors.toList());
    }

    public ParticipantDTO updateParticipant(Long id, ParticipantDTO participantDTO) {
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        modelMapper.map(participantDTO, existingParticipant);
        existingParticipant.setId(id);
        Participant updatedParticipant = participantRepository.save(existingParticipant);

        return modelMapper.map(updatedParticipant, ParticipantDTO.class);
    }

    public void deleteParticipant(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Participant not found");
        }
        participantRepository.deleteById(id);
    }
}