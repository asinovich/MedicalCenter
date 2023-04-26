package com.medical.center.appointment.mapper.impl;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.mapper.AppointmentMapper;
import com.medical.center.appointment.model.Appointment;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient_record.service.PatientRecordService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMapperImpl implements AppointmentMapper {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PatientRecordService patientRecordService;

    //@Autowired
    //private RoomService roomService;

    @Override
    public Appointment mapAppointmentDtoToAppointment(AppointmentDto appointmentDto) {
        return Appointment.builder()
            .visitDateTime(appointmentDto.getVisitDateTime())
            .note(appointmentDto.getNote())
            .appointmentType(appointmentDto.getAppointmentType())
            .status(appointmentDto.getStatus())
            .employee(employeeService.getById(appointmentDto.getEmployeeId()))
            .patientRecord(patientRecordService.getById(appointmentDto.getPatientRecordId()))
            //.room(RoomService.getById(appointmentDto.getRoomId()))
            .build();
    }

    @Override
    public AppointmentDto mapAppointmentToAppointmentDto(Appointment appointment) {
        return AppointmentDto.builder()
            .visitDateTime(appointment.getVisitDateTime())
            .note(appointment.getNote())
            .appointmentType(appointment.getAppointmentType())
            .status(appointment.getStatus())
            .employeeId(appointment.getEmployee() != null ? appointment.getEmployee().getId() : null)
            .patientRecordId(appointment.getPatientRecord() != null ? appointment.getPatientRecord().getId() : null)
            //.roomId(appointment.getRoom() != null ? appointment.getRoom().getId() : null)
            .build();
    }

    @Override
    public List<Appointment> mapAppointmentDtosToAppointments(
        List<AppointmentDto> appointmentDtos) {
        return appointmentDtos.stream()
            .map(this::mapAppointmentDtoToAppointment)
            .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> mapAppointmentsToAppointmentDtos(List<Appointment> appointments) {
        return appointments.stream()
            .map(this::mapAppointmentToAppointmentDto)
            .collect(Collectors.toList());
    }
}
