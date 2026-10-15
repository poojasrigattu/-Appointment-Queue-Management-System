
package com.example.appointment_backend.repository;

import com.example.appointment_backend.entity.QueueTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QueueTicketRepository extends JpaRepository<QueueTicket, Long> {

    Optional<QueueTicket> findByAppointmentId(Long appointmentId);

    List<QueueTicket> findByStatusOrderByTicketNumberAsc(String status);
}
