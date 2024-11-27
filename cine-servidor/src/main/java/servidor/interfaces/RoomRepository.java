package servidor.interfaces;

import servidor.classes.Room;

public interface RoomRepository {
    Room findRoomById(int roomId);
}
