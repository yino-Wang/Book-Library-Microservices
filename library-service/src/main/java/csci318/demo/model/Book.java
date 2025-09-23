package csci318.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lib_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    @Column(nullable = false)
    private String isbn;

    public Book() {
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}