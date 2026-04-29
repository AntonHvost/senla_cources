import com.fasterxml.jackson.databind.ObjectMapper;
import domain.dto.TransferMessage;
import domain.model.Account;
import org.apache.kafka.clients.consumer.internals.Acknowledgements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepositoryImpl;
import repository.Repository;
import repository.TransferRepositoryImpl;

public class ConsumerService {

    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Repository accountRepository;
    private final Repository transferRepository;

    public ConsumerService(AccountRepositoryImpl accountRepository, TransferRepositoryImpl transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    @KafkaListener(topics = "bank-transfer")
    public void listenTransfer(String message) {
        log.info("Receive message from topic: {}", message);
        TransferMessage transferMessage = objectMapper.convertValue(message, TransferMessage.class);

        Account sender = accountRepository.findById(transferMessage.getFrom());
        Account receiver = objectMapper.convertValue(transferMessage.getTo(), Account.class);
    }
}
