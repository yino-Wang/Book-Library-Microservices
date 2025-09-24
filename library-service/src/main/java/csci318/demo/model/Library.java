package csci318.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Library {

    @Id
    //@GeneratedValue
    private long id;

    @Column
    private String name;

    // Build a one-to-one relationship between library and address.
    // other relationships: @ManyToOne, @OneToMany, @ManyToMany
    // CascadeType.PERSIST: save() or persist() operations cascade to related entities.
    // Reference of CascadeType:
    // https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "library_id")
    private List<Book> books = new ArrayList<>();

    public Library() {};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }

    public void addBook(String isbn, String title, String author) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        books.add(book);
    }

    // Keep the old addBook(String isbn) for backward compatibility if needed
    public void addBook(String isbn) {
        Book book = new Book();
        book.setIsbn(isbn);
        books.add(book);
    }

    public void removeBook(String isbn){
        books.removeIf(b -> b.getIsbn().equals(isbn));
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}