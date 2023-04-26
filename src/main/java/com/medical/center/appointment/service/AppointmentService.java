package com.medical.center.appointment.service;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.model.Appointment;
import java.util.List;

public interface AppointmentService {

    AppointmentDto create(AppointmentDto appointmentDto);

    AppointmentDto update(AppointmentDto appointmentDto);

    void delete(Long id);

    AppointmentDto getById(Long id);

    List<AppointmentDto> getAll();
}
