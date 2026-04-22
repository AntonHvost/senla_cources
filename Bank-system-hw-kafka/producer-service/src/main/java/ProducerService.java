import com.fasterxml.jackson.core.JsonProcessingException;
import domain.model.Account;
import jakarta.annotation.PostConstruct;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repository.AccountRepositoryImpl;
import repository.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ProducerService {
    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final Repository accountRepository;
    private final HashMap<Long, Account> accountHashMap = new HashMap<>();
    private final Random random = new Random();

    public ProducerService(KafkaTemplate<Long, String> kafkaTemplate, AccountRepositoryImpl accountRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            List<Account> newAccounts = new ArrayList<>();
            for(int i = 0; i < 1000; i++) {
                Account account = new Account();
                account.setId((long) i++);
                account.setBalance(new BigDecimal(random.nextDouble(10_00,1000000_000 + 1)));
                newAccounts.add(account);
            }
            accountRepository.save(newAccounts);
            newAccounts.forEach(account -> accountHashMap.put(account.getId(), account));
        } else {
            accounts.forEach(account -> accountHashMap.put(account.getId(), account));
        }
    }

    @Scheduled(fixedRate = 200)
    public void sendTransfer() throws JsonProcessingException {

    }


}
