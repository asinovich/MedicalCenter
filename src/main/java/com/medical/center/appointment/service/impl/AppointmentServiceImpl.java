package com.medical.center.appointment.service.impl;

import com.medical.center.appointment.dto.AppointmentDto;
import com.medical.center.appointment.mapper.AppointmentMapper;
import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.repository.AppointmentRepository;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.patient_record.service.PatientRecordService;
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
    private PatientRecordService patientRecordService;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Appointment create(Appointment appointment) {
        log.info("Save appointment={}", appointment);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(Appointment appointment) {
        log.info("Update appointment={}", appointment);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete appointment by id={}", id);
        appointmentRepository.delete(id);
    }

    @Override
    public Appointment getById(Long id) {
        log.info("Get appointment by id={}", id);
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Appointment> getByPatientId(Long patientId) {
        log.info("Get all appointments by patientId={}", patientId);
        Long patientRecordId = patientRecordService.getByPatientId(patientId).getId();
        return appointmentRepository.findByPatientRecordId(patientRecordId);
    }

    @Override
    public List<Appointment> getAll() {
        log.info("Get all appointments");
        return appointmentRepository.findAll();
    }
}
