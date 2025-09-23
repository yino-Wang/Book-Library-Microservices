package csci318.demo.service;

import csci318.demo.model.Library;
import csci318.demo.repository.LibraryRepository;
import csci318.demo.service.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository, RestTemplate restTemplate) {
        this.libraryRepository = libraryRepository;
        this.restTemplate = restTemplate;
    }

    public Library addBookToLibrary(Long id, String isbn, BookDTO bookDTO) {
        Library library = libraryRepository.findById(id).orElseThrow(RuntimeException::new);
        library.addBook(isbn);
        String bookServiceUrl = "http://localhost:8080/books/" + isbn + "/libraries/" + id + "/add";
        restTemplate.put(bookServiceUrl, bookDTO);
        return libraryRepository.save(library);
    }

    public Library removeBookFromLibrary(Long id, String isbn) {
        Library library = libraryRepository.findById(id).orElseThrow(RuntimeException::new);
        library.removeBook(isbn);
        String bookServiceUrl = "http://localhost:8080/books/" + isbn + "/libraries/" + id + "/remove";
        restTemplate.put(bookServiceUrl, null);
        return libraryRepository.save(library);
    }

    public Library borrowAtLibrary(Long id, String isbn) {
        Library library = libraryRepository.findById(id).orElseThrow(RuntimeException::new);
        library.removeBook(isbn);
        String bookServiceUrl = "http://localhost:8080/books/borrow/" + isbn + "/" + id;
        restTemplate.put(bookServiceUrl, null);
        return libraryRepository.save(library);
    }

    public Library returnAtLibrary(Long id, String isbn) {
        Library library = libraryRepository.findById(id).orElseThrow(RuntimeException::new);
        library.addBook(isbn);
        String bookServiceUrl = "http://localhost:8080/books/return/" + isbn + "/" + id;
        restTemplate.put(bookServiceUrl, null);
        return libraryRepository.save(library);
    }
}
