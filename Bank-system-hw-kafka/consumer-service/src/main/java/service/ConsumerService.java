package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.dto.TransferMessage;
import domain.enums.TransferStatus;
import domain.model.Account;
import domain.model.Transfer;
import exception.TransferError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepositoryImpl;
import repository.Repository;
import repository.TransferRepositoryImpl;

@Service
public class ConsumerService {

    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Repository<Account,Long> accountRepository;
    private final Repository<Transfer,Long> transferRepository;

    public ConsumerService(@Qualifier("accountRepositoryImpl") Repository<Account,Long> accountRepository,
                           @Qualifier("transferRepositoryImpl") Repository<Transfer,Long> transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    @KafkaListener(topics = "bank-transfers")
    public void listenTransfer(String message) throws TransferError {
        log.info("Receive message from topic: {}", message);
        TransferMessage transferMessage = objectMapper.convertValue(message, TransferMessage.class);

        Account sender = accountRepository.findById(transferMessage.getFrom()).orElse(null);
        Account receiver = accountRepository.findById(transferMessage.getTo()).orElse(null);

        try {
            if (sender != null || receiver != null) {
                if (sender.getBalance().compareTo(transferMessage.getAmount()) > 0) {
                    sender.setBalance(sender.getBalance().subtract(transferMessage.getAmount()));
                    receiver.setBalance(receiver.getBalance().add(transferMessage.getAmount()));

                    accountRepository.update(sender);
                    accountRepository.update(receiver);

                    transferRepository.save(new Transfer(
                            transferMessage.getId(),
                            sender,
                            receiver,
                            transferMessage.getAmount(),
                            TransferStatus.DONE
                    ));
                }
            }
            log.error("Transfer message from topic {} received error", message);
            transferRepository.save(new Transfer(
                    transferMessage.getId(),
                    sender,
                    receiver,
                    transferMessage.getAmount(),
                    TransferStatus.FAILED
            ));
            throw new TransferError("Transfer failed");
        } catch (Exception e) {
            log.error("Transfer message from topic {} received error", message);
            transferRepository.save(new Transfer(
                    transferMessage.getId(),
                    sender,
                    receiver,
                    transferMessage.getAmount(),
                    TransferStatus.FAILED
            ));
        }
    }
}
