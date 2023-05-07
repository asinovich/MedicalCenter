package com.medical.center.room.repository;

import com.medical.center.room.model.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findById(Long id);
}
