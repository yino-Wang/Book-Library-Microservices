package csci318.demo.service.dto;

//a container that encapsulate book info to transfer data via different layers
public class BookDTO {

    private String title;
    private String isbn;

    public BookDTO() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
