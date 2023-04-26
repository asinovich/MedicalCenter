package com.medical.center.appointment.dto;

import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private LocalDateTime visitDateTime;
    private String note;
    private AppointmentType appointmentType;
    private AppointmentStatus status;
    private Long employeeId;
    //private Long roomId;
    private Long patientRecordId;
}
