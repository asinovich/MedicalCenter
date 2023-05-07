package com.medical.center.room.service;

import com.medical.center.patient_record.model.PatientRecord;
import com.medical.center.room.model.Room;
import java.util.List;

public interface RoomService {

    Room create(Room room);

    Room update(Room room);

    void delete(Long id);

    Room getById(Long id);

    List<Room> getAll();
}
