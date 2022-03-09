package utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * <h2>SetLogger</h2>
 *
 * @author Melanie Le
 * @version 1.0
 * @since 2022-03-04
 */

public class SetLogger {
    public final static Logger log = Logger.getLogger(SetLogger.class.getName());
    private static FileHandler fh;

    /**
     * This class opens log.txt file and sets logger's format
     * when user logins successfully.
     */
    private static void setFormatLog() {
        LocalDate timeStamp = LocalDate.now();
        try {
            fh = new FileHandler("logs/" + timeStamp + "log.txt", true);
            log.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loginLog() is called when a user successfully logged into the system.
     * Type of user will be distinguished by their ID.
     * 
     * @param username
     * @param id
     */
    public static void loginLog(String username, int id) {
        try {
            setFormatLog();
            String userType;
            switch (id / 100) {
                case 1:
                    userType = "Admin";
                    break;
                case 2:
                    userType = "Librarian";
                    break;
                default:
                    userType = "Student";
                    break;
            }

            log.info(userType + " [" + id + "] " + username + " has logged into the system.");
        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A new librarian is registered successfully.
     * 
     * @param librarianName
     * @param id
     */
    public static void librarianRegistered(String librarianName, int id) {
        try {
            log.info("New librarian has been registered: " + id + " - " + librarianName);

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A librarian has been deleted successfully.
     * 
     * @param id
     */
    public static void librarianDeleted(int id) {
        try {
            log.info("Librarian " + id + " has been deleted.");

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A new book is added to the library database successfully.
     * 
     * @param bookName
     * @param bookID
     */
    public static void itemAdded(String bookName, int bookID) {
        try {
            log.info("New item: " + bookID + " - " + bookName + " has been added to the catalogue.");

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A book has been issued to student successfully by a librarian.
     * 
     * @param bookID
     * @param studentID
     */
    public static void itemIssued(int bookID, int studentID) {
        try {
            log.info("Item [" + bookID + "] has been issued to student " + studentID);

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A book is returned by a student.
     * 
     * @param bookID
     * @param studID
     */
    public static void itemReturned(int bookID, int studID) {
        try {
            log.info("Student " + studID + " returned [" + bookID + "].");

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * A request ticket of a book is issued to a student to be in waiting list.
     * 
     * @param studID
     * @param bookID
     */
    public static void bookRequestTicket(int studID, int bookID) {
        try {
            log.info("A request ticket for (" + bookID + ") has been issued to (" + studID + ").");

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }

    }

    /**
     * A report for librarians and catalogue is generated.
     * 
     * @param reportType
     * @param genDate
     */
    public static void reportGenerated(String reportType, String genDate) {
        try {
            log.info(reportType + " Report has been generated on " + genDate);

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }
    }

    /**
     * librarians and books file are saved successfully.
     * 
     * @param filename
     */
    public static void fileSaved(String filename) {
        try {
            log.info(filename + ".txt saved successfully.");

        } catch (Exception e) {
            log.log(Level.WARNING, "Exception thrown: ", e);
        }

    }
}
