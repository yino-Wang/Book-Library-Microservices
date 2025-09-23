package csci318.demo.repository;

import csci318.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
// manage book aggregate root
//JpaRepository<Book, String> is a generic interface, tell Spring Data --->
// entity is Book, the primary key type is String (isbn)
// it will generate many methods like findById, save, delete... automatically
public interface BookRepository extends JpaRepository<Book, String> {
}
