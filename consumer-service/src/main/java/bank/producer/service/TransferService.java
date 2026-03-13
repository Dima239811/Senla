package bank.producer.service;

import bank.producer.entity.Account;
import bank.producer.entity.Transfer;
import bank.producer.repo.AccountRepository;
import bank.producer.repo.TransferRepository;
import com.infy.dto.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public void process(TransferEvent event) {

        Account from = accountRepository.findById(event.getFromAccountId()).orElse(null);
        Account to = accountRepository.findById(event.getToAccountId()).orElse(null);

        if (from == null || to == null) {
            log.error("Accounts not found");
            return;
        }

        if (from.getBalance().compareTo(event.getAmount()) < 0) {
            log.error("Not enough balance");
            return;
        }

        from.setBalance(from.getBalance().subtract(event.getAmount()));
        to.setBalance(to.getBalance().add(event.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        Transfer transfer = new Transfer();
        transfer.setId(event.getId());
        transfer.setFromAccountId(event.getFromAccountId());
        transfer.setToAccountId(event.getToAccountId());
        transfer.setAmount(event.getAmount());
        transfer.setStatus("READY");

        transferRepository.save(transfer);

    }
}