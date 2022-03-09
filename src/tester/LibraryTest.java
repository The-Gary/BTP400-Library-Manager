package tester;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import library.CatalogueItem;
import library.Library;
import library.Library.AccessLevel;

public class LibraryTest {

        String booksFilePath = "E:\\_Seneca\\Semester 4 - Winter 2022\\BTP400 - Java\\Assignments\\LibraryManager\\data\\books.txt";
        String adminsFilePath = "E:\\_Seneca\\Semester 4 - Winter 2022\\BTP400 - Java\\Assignments\\LibraryManager\\data\\admins.txt";
        String librariansFilePath = "E:\\_Seneca\\Semester 4 - Winter 2022\\BTP400 - Java\\Assignments\\LibraryManager\\data\\librarians.txt";
        String studentsFilePath = "E:\\_Seneca\\Semester 4 - Winter 2022\\BTP400 - Java\\Assignments\\LibraryManager\\data\\students.txt";
        String ticketsFilePath = "E:\\_Seneca\\Semester 4 - Winter 2022\\BTP400 - Java\\Assignments\\LibraryManager\\data\\tickets.txt";

        Library lib = new Library(booksFilePath, adminsFilePath, librariansFilePath,
                        studentsFilePath, ticketsFilePath);

        ///// YOUSEF
        @Test
        public void testPopulateArrays() {
                assertFalse(Library.getCatalogue().isEmpty());
                assertFalse(Library.getAdmins().isEmpty());
                assertFalse(Library.getLibrarians().isEmpty());
                assertFalse(Library.getStudents().isEmpty());
                assertFalse(Library.getActiveTickets().isEmpty());
        }

        @Test
        public void testAuthenticate() {
                assertEquals(1, Library.authenticate(100, "pass",
                                Library.AccessLevel.admin));
                assertEquals(2, Library.authenticate(258, "pass",
                                Library.AccessLevel.librarian));
                assertEquals(3, Library.authenticate(300000, "pass",
                                Library.AccessLevel.student));
                assertEquals(-1, Library.authenticate(500, "pass",
                                Library.AccessLevel.student));
                assertEquals(0, Library.authenticate(300000, "asd",
                                Library.AccessLevel.student));
        }

        @Test
        public void testRemoveLibrarian() {
                assertTrue(Library.removeLibrarian(228));
                assertFalse(Library.removeLibrarian(000));
        }

        @Test
        public void testAddLibrarian() {
                Library.addLibrarian(
                                new library.users.Librarian(222, "pass", "fName", "lName", "email", LocalDate.now()));
                assertTrue(Library.search(222, AccessLevel.librarian) != -1);
        }

        //// MELANIE:
        @Test
        public void testSearchCatalogueByID() {

                // returns index of the first book
                assertEquals(0, Library.searchCatalogue(49564));
                assertEquals(-1, Library.searchCatalogue(00000));
        }

        @Test
        public void testSearchCatalogueByName() {

                // returns index of the first book
                assertEquals(0, Library.searchCatalogue("Fixer, The"));
                assertEquals(-1, Library.searchCatalogue("The Queen's Gambit"));
        }

        @Test
        public void testAddCatalogueItem() {
                Library.addCatalogueItem(
                                new CatalogueItem(11111, "Gone With The Wind", "Romance", CatalogueItem.Category.book,
                                                true));

                // search for ID
                assertEquals(50, Library.searchCatalogue(11111));
        }

        @Test
        public void testRemoveCatalogueItem() {
                // test with exist item
                assertTrue(Library.removeCatalogueItem(88978));
                // test with non-exist item
                assertFalse(Library.removeCatalogueItem(00000));
        }

        //// HAYKAL:
        @Test
        public void testCreateTicket() {
                library.Ticket testTicket = new library.Ticket(111, 333333, 12345, true);
                Library.createTicket(testTicket);
                assertTrue(Library.getActiveTickets().contains(testTicket));
        }

        @Test
        public void testReturnItem() {
                assertFalse(Library.returnItem("someBook", 300000));
                assertFalse(Library.returnItem("testTitle", 300001));
                assertTrue(Library.returnItem("testTitle", 300000));
        }

        @Test
        public void testSearch() {
                assertEquals(0, Library.search(100, Library.AccessLevel.admin));
                assertEquals(0, Library.search(258, Library.AccessLevel.librarian));
                assertEquals(0, Library.search(300000, Library.AccessLevel.student));
                assertEquals(-1, Library.search(101, Library.AccessLevel.admin));
                assertEquals(-1, Library.search(259, Library.AccessLevel.librarian));
                assertEquals(-1, Library.search(300001, Library.AccessLevel.student));
        }

        @Test
        public void testLogout() {
                Library.authenticate(100, "pass", Library.AccessLevel.admin);
                Library.logout();
                assertEquals(0, Library.getLoggedIn().getId());
        }
}