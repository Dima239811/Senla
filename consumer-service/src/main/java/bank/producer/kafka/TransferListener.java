package bank.producer.kafka;

import bank.producer.service.TransferService;
import com.infy.dto.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class TransferListener {

    private final TransferService transferService;

    @KafkaListener(
            topics = "transfers",
            groupId = "bank-group"
    )
    public void listen(TransferEvent event) {

        log.info("Start processing {}", event.getId());

        transferService.process(event);
    }
}
