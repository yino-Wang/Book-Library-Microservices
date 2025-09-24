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
    public ResponseEntity<Void> addBookToLibraryPut(
            @PathVariable String isbn,
            @PathVariable Long libraryId,
            @RequestBody BookDTO bookDTO) {
        System.out.println("PUT addBookToLibrary: " + bookDTO);
        try {
            return bookService.addBookToLibrary(libraryId, isbn, bookDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/books/{isbn}/libraries/{libraryId}/add")
    public ResponseEntity<Void> addBookToLibraryPost(
            @PathVariable String isbn,
            @PathVariable Long libraryId,
            @RequestBody BookDTO bookDTO) {
        System.out.println("POST addBookToLibrary: " + bookDTO);
        try {
            return bookService.addBookToLibrary(libraryId, isbn, bookDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/books/{isbn}/libraries/{libraryId}/remove")
    public void removeBookFromLibrary(@PathVariable String isbn, @PathVariable Long libraryId) {
        bookService.removeBookFromLibrary(isbn, libraryId);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.ok(createdBook);
    }
}