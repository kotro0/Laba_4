package com.example.demo.service;

import com.example.demo.dto.SessionDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Event;
import com.example.demo.model.Session;
import com.example.demo.model.Speaker;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final ModelMapper modelMapper;

    public SessionDTO createSession(SessionDTO sessionDTO) {
        Event event = eventRepository.findById(sessionDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Session session = modelMapper.map(sessionDTO, Session.class);
        session.setEvent(event);

        if (sessionDTO.getSpeakerId() != null) {
            Speaker speaker = speakerRepository.findById(sessionDTO.getSpeakerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Speaker not found"));
            session.setSpeaker(speaker);
        }

        Session savedSession = sessionRepository.save(session);
        return modelMapper.map(savedSession, SessionDTO.class);
    }

    public SessionDTO getSessionById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        return modelMapper.map(session, SessionDTO.class);
    }

    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    public List<SessionDTO> getSessionsByEventId(Long eventId) {
        return sessionRepository.findByEventId(eventId)
                .stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    public SessionDTO updateSession(Long id, SessionDTO sessionDTO) {
        Session existingSession = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        modelMapper.map(sessionDTO, existingSession);

        if (sessionDTO.getEventId() != null) {
            Event event = eventRepository.findById(sessionDTO.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
            existingSession.setEvent(event);
        }

        if (sessionDTO.getSpeakerId() != null) {
            Speaker speaker = speakerRepository.findById(sessionDTO.getSpeakerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Speaker not found"));
            existingSession.setSpeaker(speaker);
        }

        Session updatedSession = sessionRepository.save(existingSession);
        return modelMapper.map(updatedSession, SessionDTO.class);
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Session not found");
        }
        sessionRepository.deleteById(id);
    }
}