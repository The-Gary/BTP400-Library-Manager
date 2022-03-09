package library.users;

import java.util.ArrayList;

import library.CatalogueItem;

/**
 * Represents a Student Person
 * Inherits from Person
 * Adds an ArrayList to represent borrowed items
 */
public class Student extends Person {
    private ArrayList<CatalogueItem> borrowed = new ArrayList<>();

    public Student(int id, String pass, String fName, String lName, String email) {
        super(id, pass, fName, lName, email);
    }

    public Student(int id, String pass, String fName, String lName, String email, ArrayList<CatalogueItem> borrowed) {
        super(id, pass, fName, lName, email);
        this.borrowed = borrowed;
    }

    public ArrayList<CatalogueItem> getBorrowed() {
        return this.borrowed;
    }

    public void setBorrowed(ArrayList<CatalogueItem> borrowed) {
        this.borrowed = borrowed;
    }

    public void addToBorrowed(CatalogueItem item) {
        this.borrowed.add(item);
    }

    public void removeBorrowed(CatalogueItem item) {
        this.borrowed.remove(item);
    }

}
