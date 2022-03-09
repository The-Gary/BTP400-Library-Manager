package library;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

import javafx.scene.control.Label;
import library.users.Admin;
import library.users.Librarian;
import library.users.Person;
import library.users.Student;
import utilities.SetLogger;

/**
 * <h2>Library Manager</h2>
 * This class controls the flow of the Library Manager.
 * It authenticates users and executes the different actions that each user can
 * trigger
 * 
 * @author group 13
 * @version 1.0
 * 
 */
public class Library {
    /**
     * Determines who is interacting with the library system
     */
    public enum AccessLevel {
        admin,
        librarian,
        student
    }

    private static ArrayList<CatalogueItem> catalogue = new ArrayList<>();
    private static ArrayList<Admin> admins = new ArrayList<>();
    private static ArrayList<Librarian> librarians = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Ticket> activeTickets = new ArrayList<>();
    private static Person loggedIn = new Person();

    /**
     * 5-args constructor takes file path of data files and populate the appropriate
     * ArrayLists.
     * 
     * @param booksFilePath
     * @param adminsFilePath
     * @param librariansFilePath
     * @param studentsFilePath
     * @param ticketsFilePath
     */
    public Library(String booksFilePath, String adminsFilePath, String librariansFilePath, String studentsFilePath,
            String ticketsFilePath) {
        populateTickets(ticketsFilePath);
        populateCatalogue(booksFilePath);
        populateData(adminsFilePath);
        populateData(librariansFilePath);
        populateData(studentsFilePath);
    }

    ///// ADMIN: YOUSEF
    /**
     * Opens file, which could be admin, librarian, or student based on their
     * username/id.
     * Retrieves all admins/librarians/students by reading the passed-in file and
     * fills up the admins/librarians/students array.
     *
     * @param filePath
     */
    private static void populateData(String filePath) {
        int id, personType, borrowedId = 0, idx;
        String pass, fName, lName, email;
        ArrayList<CatalogueItem> borrowed = new ArrayList<>();
        File fin = new File(filePath);
        try {
            Scanner fReader = new Scanner(fin);
            String item;
            while (fReader.hasNextLine()) {
                item = fReader.nextLine();
                Scanner sParser = new Scanner(item);
                sParser.useDelimiter(",");
                while (sParser.hasNext()) {
                    id = sParser.nextInt();
                    personType = Integer.parseInt(Integer.toString(id).substring(0, 1));
                    pass = sParser.next();
                    fName = sParser.next();
                    lName = sParser.next();
                    email = sParser.next();
                    switch (personType) {
                        case 1:
                            admins.add(new Admin(id, pass, fName, lName, email));
                            break;
                        case 2:
                            LocalDate date = LocalDate.parse(sParser.next());
                            librarians.add(new Librarian(id, pass, fName, lName, email, date));
                            break;
                        case 3:
                            if (sParser.hasNext()) {
                                while (sParser.hasNext()) {
                                    borrowedId = sParser.nextInt();
                                    idx = searchCatalogue(borrowedId);
                                    borrowed.add(catalogue.get(idx));
                                }
                                students.add(new Student(id, pass, fName, lName, email, borrowed));
                            } else {
                                students.add(new Student(id, pass, fName, lName, email));
                            }
                            break;
                    }
                }
                sParser.close();
            }
            fReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Based on the AccessLevel, the method will find the ID of the relevant user
     * Then checks if the password matches the @param pass.
     * Then set the loggedIn person.
     * 
     * @param id
     * @param pass
     * @param access
     * @return true if ID and password are valid, otherwise returns false.
     */
    public static int authenticate(int id, String pass, AccessLevel access) {
        System.out.println(access.toString());
        int idx = search(id, access);
        if (idx == -1) {
            return -1;
        }
        switch (access) {
            case admin:
                if (admins.get(idx).getPassword().equals(pass)) {
                    loggedIn = admins.get(idx);
                    SetLogger.loginLog(loggedIn.getFirstName(), id);
                    return 1;
                }
                break;
            case librarian:
                if (librarians.get(idx).getPassword().equals(pass)) {
                    loggedIn = librarians.get(idx);
                    SetLogger.loginLog(loggedIn.getFirstName(), id);
                    return 2;
                }
                break;
            case student:
                if (students.get(idx).getPassword().equals(pass)) {
                    loggedIn = students.get(idx);
                    SetLogger.loginLog(loggedIn.getFirstName(), id);
                    return 3;
                }
                break;
        }
        return 0;
    }

    /**
     * Searches for the librarian's ID first. If it exists, remove the object from
     * the
     * ArrayList.
     * Then saves librarian database file.
     * 
     * @param id
     * @return true if remove successfully, false otherwise.
     */
    public static boolean removeLibrarian(int id) {
        return librarians.removeIf((librarian) -> {
            if (librarian.getId() == id) {
                SetLogger.librarianDeleted(id);
                Library.save(AccessLevel.admin);
                return true;
            }
            return false;
        });
    }

    /**
     * Add a new Librarian into the array.
     * Saves the librarian database file.
     * 
     * @param newLib
     */
    public static void addLibrarian(Librarian newLib) {
        librarians.add(newLib);
        SetLogger.librarianRegistered(newLib.getFirstName(), newLib.getId());
        save(AccessLevel.admin);
    }

    /**
     * Saves the students database file
     */
    public static void saveStudents() {
        try {
            Formatter file = new Formatter("data/students.txt");
            for (Student student : students) {
                ArrayList<CatalogueItem> burrowed = student.getBorrowed();
                String sBurrowed = null;
                if (!burrowed.isEmpty()) {
                    for (CatalogueItem item : burrowed) {
                        sBurrowed += ("," + item.getId());
                    }
                }
                if (sBurrowed != null) {
                    file.format("%d,%s,%s,%s,%s%s\n", student.getId(), student.getPassword(),
                            student.getFirstName(),
                            student.getLastName(), student.getEmail(), sBurrowed);
                } else {
                    file.format("%d,%s,%s,%s,%s\n", student.getId(), student.getPassword(),
                            student.getFirstName(),
                            student.getLastName(), student.getEmail());
                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * saves the different user databases based on @param access
     * 
     * @param access
     */
    public static void save(AccessLevel access) {
        String fileName = "";
        switch (access) {
            case admin:
                fileName = "librarians";
                break;
            case librarian:
                fileName = "books";
                break;
            case student:
                fileName = "tickets";
                break;
        }
        try {
            Formatter file = new Formatter("data/" + fileName + ".txt");

            switch (access) {
                case admin:
                    for (Librarian lib : librarians) {
                        file.format("%d,%s,%s,%s,%s,%s\n", lib.getId(), lib.getPassword(), lib.getFirstName(),
                                lib.getLastName(), lib.getEmail(), lib.getHireDate().toString());
                    }
                    break;
                case librarian:
                    for (CatalogueItem item : catalogue) {
                        file.format("%d_%s_%s_%s_%s\n", item.getId(), item.getTitle(), item.getGenre(),
                                item.getCategory(), item.isAvailable());
                    }
                    break;
                case student:
                    for (Ticket ticket : activeTickets) {
                        file.format("%d,%d,%d,%s\n", ticket.getId(), ticket.getStudentId(), ticket.getItemId(),
                                ticket.isAvailable());
                    }

                    break;
            }
            SetLogger.fileSaved(fileName);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    ///// LIBRARIAN: MELANIE
    /**
     * Searches a catalogue by ID
     * 
     * @param id
     * @return the index where catalogue placed in the array. If book does not
     *         exist, returns -1.
     */
    public static int searchCatalogue(int id) {
        int idx = -1;
        for (CatalogueItem item : catalogue) {
            ++idx;
            if (item.getId() == id)
                return idx;
        }
        return -1;
    }

    /**
     * Searches catalogue by name.
     * Compare each title with the passed-in title
     * 
     * @param itemName
     * @return the index of the item in the catalogue, otherwise, returns -1.
     */
    public static int searchCatalogue(String itemName) {
        int idx = -1;
        boolean found = false;
        for (CatalogueItem item : catalogue) {
            ++idx;
            if (item.getTitle().equals(itemName)) {
                found = true;
                break;
            }
        }
        if (found)
            return idx;
        return -1;
    }

    /**
     * Adds new book to the library.
     * Saves the catalogue database file.
     * 
     * @param newItem
     */
    public static void addCatalogueItem(CatalogueItem newItem) {
        catalogue.add(newItem);
        SetLogger.itemAdded(newItem.getTitle(), newItem.getId());
        save(AccessLevel.librarian);
    }

    /**
     * Searches for the book's ID first. If it exists, remove the object from the
     * ArrayList.
     * Then saves the catalogue database file
     * 
     * @param id
     * @return true if remove successfully, false otherwise.
     */
    public static boolean removeCatalogueItem(int id) {
        int idx = searchCatalogue(id);
        if (idx != -1) {
            catalogue.remove(idx);
            Library.save(AccessLevel.librarian);
            return true;
        }
        return false;
    }

    /**
     * Searches active ticket array, and if a match is found with @param id,
     * it checks if the item is available
     * if so, it assigns the item assigned to the ticket, to the student assigned
     * to the ticket
     * 
     * @param id
     * @param msg
     * @return true if an item is successfully assigned to a student, false
     *         otherwise
     */
    public static boolean resolveTicket(int id, Label msg) {
        int stdId;
        CatalogueItem item;
        for (Ticket ticket : activeTickets) {
            if (ticket.getId() == id) {
                stdId = ticket.getStudentId();
                item = catalogue.get(searchCatalogue(ticket.getItemId()));
                if (!item.isAvailable()) {
                    msg.setText("The requested item is not available. Ticket is not resolved.");
                    return false;
                } else {
                    students.get(search(stdId, AccessLevel.student)).addToBorrowed(item);
                    item.setAvailable(false);
                    activeTickets.remove(ticket);
                    SetLogger.itemIssued(item.getId(), stdId);
                    saveStudents();
                    save(AccessLevel.student);
                    save(AccessLevel.librarian);
                    msg.setText("Item [" + item.getId() + "] assigned to student [" + stdId + "]");
                    return true;
                }
            }
        }
        msg.setText("The ticket id was not found");
        return false;
    }

    /**
     * Generates a report file based on the @param access
     * 
     * @param access
     */
    public static void genReport(AccessLevel access) {

        String className = "";
        if (access == AccessLevel.admin) {
            className = "Librarian";
        } else if (access == AccessLevel.librarian) {
            className = "Catalogue";
        }

        try (Formatter report = new Formatter(
                "./reports/" + className + "Report" + LocalDate.now().toString() + ".txt")) {
            if (className.equals("Librarian")) {
                report.format("%s\n", className + " Report generated on " + LocalDate.now().toString() + " by admin: "
                        + loggedIn.getId() + "\n------------------------------------------------------");
                String formatString = "%-6s %-10s\n";
                report.format(formatString, "ID", "Name");
                for (Librarian librarian : librarians) {
                    report.format(formatString, librarian.getId(),
                            librarian.getFirstName() + " " + librarian.getLastName());
                }
            }
            if (className.equals("Catalogue")) {
                report.format("%s\n",
                        className + " Report generated on " + LocalDate.now().toString() + " by librarian: "
                                + loggedIn.getId() + "\n------------------------------------------------------");
                String formatString = "%-6s %-16.15s %-10.10s\n";
                report.format(formatString, "ID", "Title", "Genre");
                for (CatalogueItem item : catalogue) {
                    report.format(formatString, item.getId(),
                            item.getTitle(), item.getGenre());
                }
            }
            SetLogger.reportGenerated(className, LocalDate.now().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the catalogue file
     * Retrieves all items in the library by reading the passed-in file and fills up
     * the catalogue array.
     * 
     * @param booksFilePath
     */
    private static void populateCatalogue(String booksFilePath) {
        int id;
        String title, genre, category;
        CatalogueItem.Category cat;
        try {
            File fin = new File(booksFilePath);
            Scanner fReader = new Scanner(fin);
            while (fReader.hasNextLine()) {
                String item = fReader.nextLine();
                Scanner sParser = new Scanner(item);
                sParser.useDelimiter("_");
                while (sParser.hasNext()) {
                    id = sParser.nextInt();
                    title = sParser.next();
                    genre = sParser.next();
                    category = sParser.next();
                    switch (category) {
                        case "book":
                            cat = CatalogueItem.Category.book;
                            break;
                        case "magazine":
                            cat = CatalogueItem.Category.magazine;
                            break;
                        case "movie":
                            cat = CatalogueItem.Category.movie;
                            break;
                        default:
                            cat = CatalogueItem.Category.unavailable;
                            break;
                    }
                    boolean avail = sParser.nextBoolean();
                    catalogue.add(new CatalogueItem(id, title, genre, cat, avail));
                }
                sParser.close();
            }
            fReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///// STUDENT: HAYKAL
    /**
     * Checks the @param itemTitle and if a result is found
     * it will remove that items from the @param studentId's borrowed list
     * also sets the items status to available in the catalogue
     * 
     * @param itemTitle
     * @param studentId
     * @return true if the item is returned successfully, false otherwise
     */
    public static boolean returnItem(String itemTitle, int studentId) {
        int itemIdx = searchCatalogue(itemTitle);
        if (itemIdx == -1)
            return false;

        CatalogueItem item = catalogue.get(itemIdx);
        item.setAvailable(true);
        int stdIdx = search(studentId, AccessLevel.student);
        SetLogger.itemReturned(item.getId(), studentId);
        if (stdIdx != -1) {
            students.get(stdIdx).removeBorrowed(item);
            save(AccessLevel.librarian);
            saveStudents();
            return true;
        }
        return false;
    }

    /**
     * Adds the @param newTicket to the activeTickets array.
     * 
     * @param newTicket
     */
    public static void createTicket(Ticket newTicket) {
        if (!activeTickets.contains(newTicket)) {
            activeTickets.add(newTicket);
            SetLogger.bookRequestTicket(newTicket.getStudentId(), newTicket.getItemId());
        }
    }

    /**
     * Sets the loggedIn property to a blank person
     */
    public static void logout() {
        loggedIn = new Person();
    }

    /**
     * Searches for Admin/Librarian/Student by ID based on the AccessLevel
     * 
     * @param id
     * @param access
     * @return array index, otherwise, -1 if not exist.
     */
    public static int search(int id, AccessLevel access) {
        int idx = -1;
        switch (access) {
            case admin:
                for (Admin admin : admins) {
                    ++idx;
                    if (admin.getId() == id)
                        return idx;
                }
                break;
            case librarian:
                for (Librarian librarian : librarians) {
                    ++idx;
                    if (librarian.getId() == id)
                        return idx;
                }
                break;
            case student:
                for (Student student : students) {
                    ++idx;
                    if (student.getId() == id)
                        return idx;
                }
                break;
        }
        return -1;
    }

    /**
     * Opens the tickets file
     * Retrieves all tickets by reading the passed-in file and fills up the
     * activeTickets array.
     * 
     * @param ticketsFilePath
     */
    private static void populateTickets(String ticketsFilePath) {
        int id, studentId, itemId;
        boolean avail;
        try {
            File fin = new File(ticketsFilePath);
            Scanner fReader = new Scanner(fin);
            while (fReader.hasNextLine()) {
                String item = fReader.nextLine();
                Scanner sParser = new Scanner(item);
                sParser.useDelimiter(",");
                while (sParser.hasNext()) {
                    id = sParser.nextInt();
                    studentId = sParser.nextInt();
                    itemId = sParser.nextInt();
                    avail = sParser.nextBoolean();
                    activeTickets.add(new Ticket(id, studentId, itemId, avail));
                }
                sParser.close();
            }
            fReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///// GENERIC METHODS
    public static Person getLoggedIn() {
        return loggedIn;
    }

    public static ArrayList<CatalogueItem> getCatalogue() {
        return catalogue;
    }

    public static ArrayList<Ticket> getActiveTickets() {
        return activeTickets;
    }

    public static ArrayList<Librarian> getLibrarians() {
        return librarians;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public static ArrayList<Admin> getAdmins() {
        return admins;
    }

}