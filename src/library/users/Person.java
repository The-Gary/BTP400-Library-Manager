package library.users;

/**
 * Represents a Person. Attributes of this class are shared among the inherited
 * classes
 */
public class Person {
    private final int id;
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";

    public Person() {
        id = 0;
    }

    public Person(int id, String password, String firstName, String lastName, String email) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
