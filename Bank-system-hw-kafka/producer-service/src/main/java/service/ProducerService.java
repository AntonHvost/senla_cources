package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.dto.TransferMessage;
import domain.model.Account;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ProducerService {
    private final Logger log = LoggerFactory.getLogger(ProducerService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Repository<Account,Long> accountRepository;
    private final HashMap<Long, Account> accountHashMap = new HashMap<>();
    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate,
                           @Qualifier("accountRepositoryImpl") Repository<Account,Long> accountRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            for(int i = 0; i < 1000; i++) {
                Account account = new Account();
                account.setBalance(new BigDecimal(random.nextDouble(10_00,1000000_000 + 1)));

                accountRepository.save(account);
            }
            accounts = accountRepository.findAll();
            accounts.forEach(account -> accountHashMap.put(account.getId(), account));
        } else {
            accounts.forEach(account -> accountHashMap.put(account.getId(), account));
        }
    }

    @Transactional
    @Scheduled(fixedRate = 200)
    public void sendTransfer() throws JsonProcessingException {
        TransferMessage msg = new TransferMessage();

        List<Long> accountIds = new ArrayList<>(accountHashMap.keySet());

        Long from_id_rand_num = accountIds.get(random.nextInt(accountIds.size()));
        Long to_id_rand_num;

        do {
            to_id_rand_num = accountIds.get(random.nextInt(accountIds.size()));
        }
        while (from_id_rand_num.equals(to_id_rand_num));

        msg.setId(random.nextLong(2000));
        msg.setFrom(from_id_rand_num);
        msg.setTo(to_id_rand_num);
        msg.setAmount(BigDecimal.valueOf(random.nextInt(1,500),2));

        String json = objectMapper.writeValueAsString(msg);
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("bank-transfers", null, json);
            return null;
        });
        log.info("Send transfer message to kafka");
    }


}
