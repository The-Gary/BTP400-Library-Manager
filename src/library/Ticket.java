package library;

/**
 * Represents a request ticket for an Item
 * It holds which student the ticket belongs to
 * and which item has been requested
 * and weather the requested item is available or not
 */
public class Ticket {
    private final int id;
    private final int studentId;
    private final int itemId;
    private boolean available;
    private static int idGen = 1;

    public Ticket(int id, int studentId, int itemId, boolean isAvailable) {
        this.id = id;
        this.studentId = studentId;
        this.itemId = itemId;
        this.available = isAvailable;
        ++idGen;
    }

    public int getId() {
        return this.id;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public int getItemId() {
        return this.itemId;
    }

    public static int generateId() {
        return idGen++;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean itemAvailable) {
        this.available = itemAvailable;
    }

}
