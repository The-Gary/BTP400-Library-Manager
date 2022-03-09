package library.users;

/**
 * Represents an Admin Person
 * Inherits from Person
 */
public class Admin extends Person {
    public Admin(int id, String pass, String fName, String lName, String email) {
        super(id, pass, fName, lName, email);
    }
}