package bookstore.util;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LibraryConfig {

    @Value("${book.stale.months}")
    private int staleMonths;

    @Value("${book.auto.close.requests}")
    private boolean autoCloseRequests;
}
