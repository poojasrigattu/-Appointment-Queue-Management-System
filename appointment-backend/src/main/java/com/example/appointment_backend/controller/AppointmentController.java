
package com.example.appointment_backend.controller;

import com.example.appointment_backend.dto.AppointmentRequest;
import com.example.appointment_backend.dto.AppointmentResponse;
import com.example.appointment_backend.entity.Appointment;
import com.example.appointment_backend.entity.Subcategory;
import com.example.appointment_backend.entity.User;
import com.example.appointment_backend.repository.AppointmentRepository;
import com.example.appointment_backend.repository.SubcategoryRepository;
import com.example.appointment_backend.repository.UserRepository;
import com.example.appointment_backend.service.QueueNumberService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final QueueNumberService queueNumberService;

    public AppointmentController(
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            SubcategoryRepository subcategoryRepository,
            QueueNumberService queueNumberService) {

        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.queueNumberService = queueNumberService;
    }

    // BOOK A NEW APPOINTMENT
    @PostMapping
    @Transactional
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        // Find the user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found. Please log in again."));

        // Find the selected service
        Subcategory subcategory = subcategoryRepository
                .findById(request.getSubcategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Selected service was not found."));

        // Prevent duplicate booking for the same service, date and time
        boolean slotAlreadyBooked = appointmentRepository
                .existsBySubcategoryIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                        request.getSubcategoryId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime(),
                        "CANCELLED");

        if (slotAlreadyBooked) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This time slot is already booked for the selected service. "
                            + "Please choose another time.");
        }

        // Create appointment
        Appointment appointment = new Appointment();

        appointment.setUser(user);
        appointment.setSubcategory(subcategory);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus("BOOKED");
        appointment.setQueueStatus("WAITING");

        // Generate unique appointment number
        String appointmentNumber;

        do {
            appointmentNumber = "APT-"
                    + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();
        } while (appointmentRepository
                .existsByAppointmentNumber(appointmentNumber));

        appointment.setAppointmentNumber(appointmentNumber);

        // Save appointment
        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        // Recalculate queue numbers for this service and date
        queueNumberService.renumberQueue(
                subcategory.getId(),
                request.getAppointmentDate());

        // Reload appointment to get the updated queue number
        Appointment updatedAppointment = appointmentRepository
                .findById(savedAppointment.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found after booking."));

        // Return the booked appointment
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AppointmentResponse(updatedAppointment));
    }

    // GET ALL ACTIVE APPOINTMENTS
    @GetMapping("/queue")
    public ResponseEntity<List<AppointmentResponse>> getQueueList() {

        List<AppointmentResponse> queueList = appointmentRepository
                .findByStatusNotOrderByAppointmentDateAscAppointmentTimeAsc(
                        "CANCELLED")
                .stream()
                .map(AppointmentResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(queueList);
    }
}
