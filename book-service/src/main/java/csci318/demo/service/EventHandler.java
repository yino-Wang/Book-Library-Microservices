package csci318.demo.service;

import csci318.demo.model.event.BookEvent;
import csci318.demo.repository.BookEventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
/*listen and handle the events that published
* */
@Service
public class EventHandler {

    private final BookEventRepository bookEventRepository;

    EventHandler(BookEventRepository bookEventRepository) {
        this.bookEventRepository = bookEventRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
            condition = "#bookEvent.eventName == 'borrow'")
    public void handleBorrowEvent(BookEvent bookEvent){
        bookEventRepository.save(bookEvent);
        System.out.println(bookEvent);
    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
            condition = "#bookEvent.eventName == 'return'")
    @EventListener
    public void handleReturnEvent(BookEvent bookEvent) {
        bookEventRepository.save(bookEvent);
        System.out.println(bookEvent);
    }
}
