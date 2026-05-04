package service;

import domain.model.impl.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.Repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {

    @Mock
    private Repository<Consumer, Long> consumerRepository;

    @InjectMocks
    private ConsumerService consumerService;

    @Test
    void findConsumerById_returnsOptional_whenPresent() {
        Consumer c = new Consumer("n", "p", "e");
        c.setId(5L);
        when(consumerRepository.findById(5L)).thenReturn(Optional.of(c));

        Optional<Consumer> result = consumerService.findConsumerById(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
    }

    @Test
    void findConsumerById_returnsEmpty_whenMissing() {
        when(consumerRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(consumerService.findConsumerById(99L).isEmpty());
    }

    @Test
    void findAllConsumers_returnsList() {
        when(consumerRepository.findAll()).thenReturn(List.of(new Consumer()));

        assertEquals(1, consumerService.findAllConsumers().size());
    }

    @Test
    void save_callsRepository() {
        Consumer c = new Consumer();
        consumerService.save(c);
        verify(consumerRepository).save(c);
    }

    @Test
    void update_callsRepository() {
        Consumer c = new Consumer();
        c.setId(1L);
        consumerService.update(c);
        verify(consumerRepository).update(c);
    }
}
