package library;

/**
 * Represents an item in the Library Catalogue; book, magazine, movie...
 */
public class CatalogueItem {
    /**
     * Determinse the category of an item
     */
    public enum Category {
        book,
        magazine,
        movie,
        unavailable
    }

    private final int id;
    private String title;
    private String genre;
    private Category category;
    private boolean available;

    public CatalogueItem(int id, String title, String genre, Category cat, boolean avail) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.category = cat;
        this.available = avail;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCategory() {
        switch (category) {
            case book:
                return "book";
            case magazine:
                return "magazine";
            case movie:
                return "movie";
            default:
                return "unavailable";
        }
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
