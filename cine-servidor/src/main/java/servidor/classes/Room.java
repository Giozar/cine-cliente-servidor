package servidor.classes;

public class Room {
    private int id;
    private int number;
    private int totalSeats;

    public Room(int id, int number, int totalSeats) {
        this.id = id;
        this.number = number;
        this.totalSeats = totalSeats;
    }

    // Getters
    public int getId() { return id; }
    public int getNumber() { return number; }
    public int getTotalSeats() { return totalSeats; }

    @Override
    public String toString() {
        return "Sala " + number + " (ID: " + id + "), Asientos totales: " + totalSeats;
    }
}
