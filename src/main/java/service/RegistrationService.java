package com.example.demo.service;

import com.example.demo.dto.RegistrationDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ParticipantRepository participantRepository;
    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Registration registerParticipant(Long participantId, Long sessionId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // Проверяем доступность мест
        if (!session.hasAvailableSeats()) {
            throw new IllegalStateException("Session is full");
        }

        // Проверяем пересечение времени
        List<Registration> existingRegistrations = registrationRepository
                .findParticipantRegistrationsInTimeRange(
                        participantId,
                        session.getStartTime(),
                        session.getEndTime()
                );

        if (!existingRegistrations.isEmpty()) {
            throw new IllegalStateException("Time conflict with existing registration");
        }

        // Создаем регистрацию
        Registration registration = new Registration(participant, session);
        registration.setStatus(RegistrationStatus.CONFIRMED);

        // Увеличиваем счетчик участников
        session.incrementParticipants();
        sessionRepository.save(session);

        Registration savedRegistration = registrationRepository.save(registration);
        return savedRegistration;
    }

    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

        registration.setStatus(RegistrationStatus.CANCELLED);

        // Уменьшаем счетчик участников
        Session session = registration.getSession();
        session.decrementParticipants();
        sessionRepository.save(session);

        registrationRepository.save(registration);
    }

    public List<Registration> getRegistrationsByParticipant(Long participantId) {
        return registrationRepository.findByParticipantId(participantId);
    }

    public List<Registration> getRegistrationsBySession(Long sessionId) {
        return registrationRepository.findBySessionId(sessionId);
    }

    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
    }
}