package csci318.demo.controller;

import csci318.demo.model.Library;
import csci318.demo.repository.BookRepository;
import csci318.demo.service.BookService;
import csci318.demo.service.dto.BookDTO;
import csci318.demo.service.dto.LibraryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //means when user access /book, it will call bookService.getAllBooks()
    @GetMapping("/books")
    List<BookDTO> allBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{isbn}")
    BookDTO findBook(@PathVariable String isbn) {
        return bookService.getBook(isbn);
    }


    @GetMapping("/books/{isbn}/available") //List<LibraryDTO> includes multiple library info
    List<LibraryDTO> availableLibraries(@PathVariable String isbn) {
        return bookService.getAvailableLibraries(isbn);
    }

    @PutMapping("/books/borrow/{isbn}/{libraryId}")
    void borrow(@PathVariable String isbn, @PathVariable String libraryId) {
        bookService.borrowBook(isbn, Long.parseLong(libraryId));
    }

    //return book to libraryId
    @PutMapping("/books/return/{isbn}/{libraryId}")
    void return1(@PathVariable String isbn, @PathVariable String libraryId) {
        bookService.returnBook(isbn, Long.parseLong(libraryId));
    }

    @PutMapping("/books/{isbn}/libraries/{libraryId}/add")
    public ResponseEntity<Void> addBookToLibrary(
            @PathVariable String isbn,
            @PathVariable Long libraryId,
            @RequestBody BookDTO bookDTO) {
        return bookService.addBookToLibrary(libraryId, isbn, bookDTO);
    }

    @PutMapping("/books/{isbn}/libraries/{libraryId}/remove")
    public void removeBookFromLibrary(@PathVariable String isbn, @PathVariable Long libraryId) {
        bookService.removeBookFromLibrary(isbn, libraryId);
    }
}