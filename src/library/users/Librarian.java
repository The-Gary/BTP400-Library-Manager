package library.users;

import java.time.LocalDate;

/**
 * Represents a Librarian Person
 * Inherits from Person
 */
public class Librarian extends Person {
    private LocalDate hireDate;

    public Librarian(int id, String pass, String fName, String lName, String email, LocalDate hireDate) {
        super(id, pass, fName, lName, email);
        this.hireDate = hireDate;
    }

    public LocalDate getHireDate() {
        return this.hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}
