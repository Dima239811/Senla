package bank.producer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class Transfer {

    @Id
    private UUID id;

    private Long fromAccountId;

    private Long toAccountId;

    private BigDecimal amount;

    private String status;

}
