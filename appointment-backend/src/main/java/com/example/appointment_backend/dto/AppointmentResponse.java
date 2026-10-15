
package com.example.appointment_backend.dto;

import com.example.appointment_backend.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {

    private Long id;
    private String appointmentNumber;
    private String serviceName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private Integer queueNumber;
    private String queueStatus;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.appointmentNumber = appointment.getAppointmentNumber();
        this.serviceName = appointment.getSubcategory().getName();
        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentTime = appointment.getAppointmentTime();
        this.status = appointment.getStatus();
        this.queueNumber = appointment.getQueueNumber();
        this.queueStatus = appointment.getQueueStatus();
    }

    public Long getId() {
        return id;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public Integer getQueueNumber() {
        return queueNumber;
    }

    public String getQueueStatus() {
        return queueStatus;
    }
}
