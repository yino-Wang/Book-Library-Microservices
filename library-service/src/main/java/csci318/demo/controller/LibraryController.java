package csci318.demo.controller;

import csci318.demo.model.Address;
import csci318.demo.model.Library;
import csci318.demo.repository.AddressRepository;
import csci318.demo.repository.LibraryRepository;
import csci318.demo.service.LibraryService;
import csci318.demo.service.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class LibraryController {

    private final LibraryRepository libraryRepository;
    private final AddressRepository addressRepository;
    private final RestTemplate restTemplate;
    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryRepository libraryRepository, AddressRepository addressRepository, RestTemplate restTemplate, LibraryService libraryService) {
        this.libraryRepository = libraryRepository;
        this.addressRepository = addressRepository;
        this.restTemplate = restTemplate;
        this.libraryService = libraryService;
    }

    @GetMapping("/libraries")
    List<Library> all() {
        return libraryRepository.findAll();
    }

    @GetMapping("/libraries/{id}")
    Library findLibraryById(@PathVariable Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @PostMapping("/libraries")
    Library createLibrary(@RequestBody Library newLibrary) {
        return libraryRepository.save(newLibrary);
    }

    @PutMapping("/libraries/{id}")
    Library updateLibraryName(@PathVariable Long id, @RequestBody Library library) {
        Library library1 = libraryRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        library1.setName(library.getName());
        return libraryRepository.save(library1);
    }

    @PutMapping("/libraries/{id}/address/{addressId}")
    Library updateLibraryAddress(@PathVariable Long id, @PathVariable Long addressId) {
        Library library = libraryRepository.findById(id).orElseThrow(RuntimeException::new);
        Address address = addressRepository.findById(addressId).orElseThrow(RuntimeException::new);
        library.setAddress(address);
        return libraryRepository.save(library);
    }

    @PutMapping("/libraries/{id}/addBook/{isbn}")
    public ResponseEntity<Void> addBookToLibrary(@PathVariable Long id, @PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        try {
            libraryService.addBookToLibrary(id, isbn, bookDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/libraries/{id}/removeBook/{isbn}")
    public Library removeBookFromLibrary(@PathVariable Long id, @PathVariable String isbn) {
        return libraryService.removeBookFromLibrary(id, isbn);
    }

    @PutMapping("/libraries/{id}/books/{isbn}/borrow")
    public Library borrowAtLibrary(@PathVariable Long id, @PathVariable String isbn) {
        return libraryService.borrowAtLibrary(id, isbn);
    }

    @PutMapping("/libraries/{id}/books/{isbn}/return")
    public Library returnAtLibrary(@PathVariable Long id, @PathVariable String isbn) {
        return libraryService.returnAtLibrary(id, isbn);
    }

}
