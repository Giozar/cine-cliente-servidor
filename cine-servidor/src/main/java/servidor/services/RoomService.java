package servidor.services;

import servidor.classes.Room;
import servidor.interfaces.RoomRepository;

public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoomById(int roomId) {
        return roomRepository.findRoomById(roomId);
    }
}
