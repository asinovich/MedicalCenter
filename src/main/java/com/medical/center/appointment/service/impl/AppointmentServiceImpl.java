package com.medical.center.appointment.service.impl;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.repository.AppointmentRepository;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.base.constant.Constant;
import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;
import com.medical.center.patient_record.service.PatientRecordService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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

    @Override
    public List<String> getEmployeeFreeTime(Long employeeId, AppointmentType type, LocalDate visitDate, Long appointmentId) {

        List<LocalTime> providerFreeTime = getProviderFreeTimeFiveMinutesInInterval(employeeId, visitDate,  appointmentId);

        providerFreeTime = getProviderFreeTimeWithCheckAppointmentDuration(providerFreeTime, getDuration(type)).stream()
            .filter(it -> it.getMinute() % Constant.TIME_INTERVAL_FOR_TIME_OUTPUT_IN_MINUTES == 0)
            .collect(Collectors.toList());

        return providerFreeTime.stream()
            .map(LocalTime::toString)
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasEmployeeFreeTime(Long employeeId, LocalDateTime visitDateTime, AppointmentType type, Long appointmentId) {
        List<LocalTime> providerFreeTime = getProviderFreeTimeFiveMinutesInInterval(employeeId, visitDateTime.toLocalDate(), appointmentId);

        List<LocalTime> plannedAppointment = getTimeInterval(
            visitDateTime.toLocalTime(),
            visitDateTime.plusMinutes(getDuration(type)).toLocalTime()).collect(Collectors.toList());

        return providerFreeTime.containsAll(plannedAppointment);
    }

    private List<LocalTime> getProviderFreeTimeFiveMinutesInInterval(Long employeeId, LocalDate visitDate, Long appointmentId) {

        List<LocalTime> timeOfAppointments = getProviderBusyTimeByProviderAndVisitDate(employeeId, visitDate, appointmentId);

        return getClinicWorkTimeList().stream()
            .filter(it -> !timeOfAppointments.contains(it))
            .collect(Collectors.toList());
    }

    private List<LocalTime> getProviderBusyTimeByProviderAndVisitDate(Long employeeId, LocalDate visitDate, Long appointmentId) {
        if (appointmentId != null) {
            return getByEmployeeIdAndVisitDateAndStatusIsNotCanceled(employeeId, visitDate)
                .stream()
                .filter(it -> {
                    if (it.getId() != appointmentId) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .map(it -> getTimeInterval(
                    it.getVisitDateTime().minusMinutes(Constant.PROVIDER_TIME_OFF_IN_MINUTES).toLocalTime(),
                    it.getVisitDateTime().plusMinutes(getDuration(it.getAppointmentType()))
                        .plusMinutes(Constant.PROVIDER_TIME_OFF_IN_MINUTES).toLocalTime()))
                .flatMap(x -> x)
                .collect(Collectors.toList());
        } else {
            return getByEmployeeIdAndVisitDateAndStatusIsNotCanceled(employeeId, visitDate)
                .stream()
                .map(it -> getTimeInterval(
                    it.getVisitDateTime().minusMinutes(Constant.PROVIDER_TIME_OFF_IN_MINUTES).toLocalTime(),
                    it.getVisitDateTime().plusMinutes(getDuration(it.getAppointmentType()))
                        .plusMinutes(Constant.PROVIDER_TIME_OFF_IN_MINUTES).toLocalTime()))
                .flatMap(x -> x)
                .collect(Collectors.toList());
        }
    }

    public List<Appointment> getByEmployeeIdAndVisitDateAndStatusIsNotCanceled(Long employeeId, LocalDate visitDate) {
        return appointmentRepository.findByEmployeeIdAndStatusIsNot(
            employeeId,
            AppointmentStatus.CANCELED
        ).stream().filter(it -> {
            if (it.getVisitDateTime().isBefore(LocalDateTime.of(visitDate, LocalTime.MIN)) || it.getVisitDateTime().isAfter(LocalDateTime.of(visitDate, LocalTime.MAX))) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toList());
    }

    public List<LocalTime> getClinicWorkTimeList() {
        return getTimeInterval(LocalTime.of(9,0), LocalTime.of(18,0)).collect(Collectors.toList());
    }

    private List<LocalTime> getProviderFreeTimeWithCheckAppointmentDuration(
        List<LocalTime> providerFreeTime, int appointmentDuration) {

        if (providerFreeTime.isEmpty()) {
            return Collections.emptyList();
        }

        for (var freeTime : providerFreeTime) {
            List<LocalTime> times = getTimeInterval(freeTime, freeTime.plusMinutes(appointmentDuration))
                .collect(Collectors.toList());
            if (!providerFreeTime.containsAll(times)) {
                providerFreeTime = providerFreeTime.stream()
                    .filter(it -> !it.equals(freeTime))
                    .collect(Collectors.toList());
            }
        }

        return providerFreeTime;
    }

    public Stream<LocalTime> getTimeInterval(LocalTime startTime, LocalTime endTime) {
        long duration = Duration.between(startTime, endTime).toMinutes();

        return IntStream.iterate(0, i -> i + Constant.TIME_INTERVAL_OF_FIVE_MINUTES)
            .limit(duration / Constant.TIME_INTERVAL_OF_FIVE_MINUTES)
            .mapToObj(startTime::plusMinutes);
    }

    private int getDuration(AppointmentType type) {
        switch (type) {
            case VISIT:
                return Constant.VISIT_IN_MINUTES;
            case CONSULTATION:
                return Constant.CONSULTATION_IN_MINUTES;
            case SURGERY:
                return Constant.SURGERY_IN_MINUTES;
            default:
                return 0;
        }
    }
}
