package com.medical.center.appointment.service.impl;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.mapper.AppointmentMapper;
import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.repository.AppointmentRepository;
import com.medical.center.appointment.service.AppointmentService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDto create(AppointmentDto appointmentDto) {
        log.info("Save appointment={}", appointmentDto);
        Appointment appointment = appointmentMapper.mapAppointmentDtoToAppointment(appointmentDto);

        return appointmentMapper.mapAppointmentToAppointmentDto(
            appointmentRepository.save(appointment)
        );
    }

    @Override
    public AppointmentDto update(AppointmentDto appointmentDto) {
        log.info("Update appointment={}", appointmentDto);
        Appointment appointment = appointmentMapper.mapAppointmentDtoToAppointment(appointmentDto);

        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public AppointmentDto getById(Long id) {
        return null;
    }

    @Override
    public List<AppointmentDto> getAll() {
        return null;
    }
}
