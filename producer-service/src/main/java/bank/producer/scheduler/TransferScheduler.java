package bank.producer.scheduler;

import bank.producer.service.AccountService;
import com.infy.dto.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferScheduler {

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;
    private final AccountService accountService;

    @Scheduled(fixedDelay = 200)
    public void generateTransfer() {

        Long from = accountService.getRandomAccountId();
        Long to = accountService.getRandomAccountId();

        TransferEvent event = new TransferEvent();

        event.setId(UUID.randomUUID());
        event.setFromAccountId(from);
        event.setToAccountId(to);
        event.setAmount(BigDecimal.valueOf(new Random().nextInt(100)));

        kafkaTemplate.send("transfers", event);

        log.info("Sent transfer {}", event.getId());
    }
}
