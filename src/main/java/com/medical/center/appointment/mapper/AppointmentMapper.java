package com.medical.center.appointment.mapper;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.model.Appointment;
import java.util.List;

public interface AppointmentMapper {

    Appointment mapAppointmentDtoToAppointment(AppointmentDto appointmentDto);

    AppointmentDto mapAppointmentToAppointmentDto(Appointment appointment);

    List<Appointment> mapAppointmentDtosToAppointments(List<AppointmentDto> appointmentDtos);

    List<AppointmentDto> mapAppointmentsToAppointmentDtos(List<Appointment> appointments);
}
