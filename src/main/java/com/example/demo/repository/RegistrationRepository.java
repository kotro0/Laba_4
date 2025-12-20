package com.example.demo.repository;

import com.example.demo.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByParticipantId(Long participantId);
    List<Registration> findBySessionId(Long sessionId);
    Optional<Registration> findByParticipantIdAndSessionId(Long participantId, Long sessionId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.session.id = :sessionId AND r.status = 'CONFIRMED'")
    int countConfirmedRegistrationsBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT r FROM Registration r WHERE r.participant.id = :participantId " +
            "AND r.session.event.id = :eventId")
    List<Registration> findByParticipantIdAndEventId(@Param("participantId") Long participantId,
                                                     @Param("eventId") Long eventId);

    @Query("SELECT r FROM Registration r WHERE r.participant.id = :participantId " +
            "AND r.session.startTime <= :endTime AND r.session.endTime >= :startTime")
    List<Registration> findParticipantRegistrationsInTimeRange(
            @Param("participantId") Long participantId,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime);
}