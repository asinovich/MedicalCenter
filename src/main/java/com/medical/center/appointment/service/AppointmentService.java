package com.medical.center.appointment.service;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.model.Appointment;
import com.medical.center.base.enums.AppointmentType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment update(Appointment appointment);

    void delete(Long id);

    Appointment getById(Long id);

    List<Appointment> getByPatientId(Long patientId);

    List<Appointment> getAll();

    boolean hasEmployeeFreeTime(Long employeeId, LocalDateTime visitDateTime, AppointmentType type, Long appointmentId);

    List<String> getEmployeeFreeTime(Long employeeId, AppointmentType type, LocalDate visitDate, Long appointmentId);
}
