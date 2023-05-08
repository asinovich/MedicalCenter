package com.medical.center.room.service.impl;

import com.medical.center.room.model.Room;
import com.medical.center.room.repository.RoomRepository;
import com.medical.center.room.service.RoomService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room create(Room room) {
        log.info("Save room={}", room);
        return roomRepository.save(room);
    }

    @Override
    public Room update(Room room) {
        log.info("Update room={}", room);
        return roomRepository.save(room);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete room by id={}", id);
        roomRepository.delete(id);
    }

    @Override
    public Room getById(Long id) {
        log.info("Get room by id={}", id);
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room getByName(String name) {
        log.info("Get room by name={}", name);
        return roomRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Room> getAll() {
        log.info("Get all rooms");
        return roomRepository.findAll();
    }
}
