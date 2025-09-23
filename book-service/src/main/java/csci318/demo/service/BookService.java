package csci318.demo.service;

import csci318.demo.model.Book;
import csci318.demo.model.Library;
import csci318.demo.model.event.BookEvent;
import csci318.demo.repository.BookRepository;
import csci318.demo.service.dto.BookDTO;
import csci318.demo.service.dto.LibraryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
/* find and save aggregate root; call external microservices using RestTemplate
   event boundary and event publish using ApplicationEventPublisher
   DTO mapping --> transfer Book/Library to BookDTO/LibraryDTO and return Controller
* */

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate; //HTTP client, sync library microservice
    private final ApplicationEventPublisher applicationEventPublisher;

    BookService(BookRepository bookRepository, RestTemplate restTemplate,
                ApplicationEventPublisher applicationEventPublisher) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    //read all books from repository --> mapping them as BookDTO --> return to controller
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> {
                    BookDTO bookDto = new BookDTO();
                    bookDto.setIsbn(book.getIsbn());
                    bookDto.setTitle(book.getTitle());
                    return bookDto;
                }).collect(Collectors.toList());
    }

    public BookDTO getBook(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(RuntimeException::new);
        BookDTO bookDto = new BookDTO();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        return bookDto;
    }
/*通过 bookRepository.findById(isbn) 从 book-service 的数据库 把这本书取出来；

调 getAvailableLibraries() 拿到这本书在哪些图书馆可借的 ID 列表（List<Long>），这是 book-service 里的领域状态；

逐个遍历这些 ID，拼成请求：GET http://localhost:8081/libraries/{id}；

用 RestTemplate.getForObject(...) 发送 HTTP GET 到 library-service 的控制器 LibraryController.findLibraryById；

把 library-service 返回的 JSON 自动反序列化成 Library 对象（Jackson 完成映射），加入 libraries 列表；

通常你随后会把这些 Library 再映射成 LibraryDTO 返回给调用方*/
    public List<LibraryDTO> getAvailableLibraries(String isbn) {
        final String url = "http://localhost:8081/libraries/";
        List<Library> libraries = new ArrayList<>();
        List<Long> libraryIds = bookRepository.findById(isbn).orElseThrow(RuntimeException::new)
                .getAvailableLibraries();
        for (Long id : libraryIds) {
            libraries.add(restTemplate.getForObject(url + id, Library.class));
        }
        return libraries.stream()
                .map(lib -> {
                    LibraryDTO libDto = new LibraryDTO();
                    libDto.setLibraryName(lib.getName());
                    libDto.setPostcode(Long.toString(lib.getId()));
                    return libDto;
                }).collect(Collectors.toList());
    }


    public void borrowBook(String isbn, long libraryId) {
        // load the aggregate root through BookRepository and modify the status only through
        //its domain method --> borrowFrom() and then save() persist
        Book book = bookRepository.findById(isbn).orElseThrow(RuntimeException::new);
        book.borrowFrom(libraryId); //domain method
        bookRepository.save(book);
        //System.out.println(book.getAvailableLibraries());
        //cuz domain.Book already have registerEvent(event), so we no need to publish event manually
        //call Library microservice to update
        String url = "http://localhost:8081" + "/libraries/" + libraryId + "/books/" + isbn + "/borrow";
        restTemplate.put(url, null);
    }

    public void returnBook(String isbn, long libraryId) {
        Book book = bookRepository.findById(isbn).orElseThrow(RuntimeException::new);
        book.returnTo(libraryId);
        bookRepository.save(book);
        BookEvent event = new BookEvent();
        event.setEventName("return");
        event.setBookIsbn(book.getIsbn());
        event.setBookTitle(book.getTitle());
        event.setLibraryId(libraryId);
        //
        applicationEventPublisher.publishEvent(event);
        //call Library microservice to update
        String url = "http://localhost:8081" + "/libraries/" + libraryId + "/books/" + isbn + "/return";
        restTemplate.put(url, null);
    }

    public ResponseEntity<Void> addBookToLibrary(Long libraryId, String isbn, BookDTO bookDTO) {
        // 1. 全局新建一本书（如果不存在）
        if (!bookRepository.existsById(isbn)) {
            Book book = new Book();
            book.setIsbn(bookDTO.getIsbn());
            book.setTitle(bookDTO.getTitle());
            // 可根据需要设置其他属性
            bookRepository.save(book);
        }
        // 2. 同步到 library-service
        String url = "http://localhost:8081/libraries/" + libraryId + "/addBook/" + isbn;
        restTemplate.put(url, bookDTO);
        return ResponseEntity.ok().build();
    }

    public void removeBookFromLibrary(String isbn, Long libraryId) {
        String url = "http://localhost:8081/libraries/" + libraryId + "/removeBook/" + isbn;
        restTemplate.put(url, null);
    }

}
