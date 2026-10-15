
package com.example.appointment_backend.repository;

import com.example.appointment_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // Get appointments belonging to a user
    List<Appointment> findByUserId(Long userId);

    // Get appointments for a specific date
    List<Appointment> findByAppointmentDate(
            LocalDate appointmentDate
    );

    // Check whether an appointment number already exists
    boolean existsByAppointmentNumber(String appointmentNumber);

    // Check for duplicate active bookings
    boolean existsBySubcategoryIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Long subcategoryId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String status
    );

    // Count appointments for a service on a particular date
    long countBySubcategoryIdAndAppointmentDateAndStatusNot(
            Long subcategoryId,
            LocalDate appointmentDate,
            String status
    );

    // Get all non-cancelled appointments in date and time order
    List<Appointment>
    findByStatusNotOrderByAppointmentDateAscAppointmentTimeAsc(
            String status
    );

    // Get appointments for one service and date,
    // sorted by appointment time and then ID
    List<Appointment>
    findBySubcategoryIdAndAppointmentDateAndStatusNotOrderByAppointmentTimeAscIdAsc(
            Long subcategoryId,
            LocalDate appointmentDate,
            String status
    );
}
