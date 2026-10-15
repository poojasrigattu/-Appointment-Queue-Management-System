
package com.example.appointment_backend.service;

import com.example.appointment_backend.entity.Appointment;
import com.example.appointment_backend.repository.AppointmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class QueueNumberService {

    private final AppointmentRepository appointmentRepository;

    public QueueNumberService(
            AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void renumberQueue(
            Long subcategoryId,
            LocalDate appointmentDate) {

        List<Appointment> appointments =
                appointmentRepository
                        .findBySubcategoryIdAndAppointmentDateAndStatusNotOrderByAppointmentTimeAscIdAsc(
                                subcategoryId,
                                appointmentDate,
                                "CANCELLED"
                        );

        int queueNumber = 1;

        for (Appointment appointment : appointments) {
            appointment.setQueueNumber(queueNumber);
            queueNumber++;
        }

        appointmentRepository.saveAll(appointments);
    }
}
