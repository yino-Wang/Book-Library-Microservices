package csci318.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibrarySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringApplication.class, args);
	}

	@Bean(name = "libraryLoadDatabase")
	public CommandLineRunner loadDatabase(csci318.demo.repository.LibraryRepository libraryRepository) throws Exception {
		return args -> {
			csci318.demo.model.Library entry = new csci318.demo.model.Library();
			entry.setId(2500L);
			entry.setName("Wollongong City Library");
			libraryRepository.save(entry);
			System.out.println(libraryRepository.findById(2500L).orElseThrow());

			csci318.demo.model.Library entry1 = new csci318.demo.model.Library();
			entry1.setId(2522L);
			entry1.setName("UOW Library");
			libraryRepository.save(entry1);
			System.out.println(libraryRepository.findById(2522L).orElseThrow());
		};
	}

}
