package bookstore.util;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LibraryConfig {

    @Value("${book.stale.months: 3}")
    private int staleMonths;

    @Value("${book.auto.close.requests: false}")
    private boolean autoCloseRequests;
}
