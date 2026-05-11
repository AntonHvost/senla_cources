package controller;

import domain.model.impl.Consumer;
import facade.ConsumerFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ConsumerControllerTest {

    @Mock
    private ConsumerFacade consumerFacade;

    @InjectMocks
    private ConsumerController consumerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consumerController).build();
    }

    @Test
    void getConsumers_returnsConsumerList() throws Exception {
        List<Consumer> consumers = createSampleConsumers();
        when(consumerFacade.getConsumers()).thenReturn(consumers);

        mockMvc.perform(get("/api/consumers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(consumerFacade, times(1)).getConsumers();
        verifyNoMoreInteractions(consumerFacade);
    }

    @Test
    void getConsumers_returnsConsumerList_whenEmptyList() throws Exception {
        when(consumerFacade.getConsumers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/consumers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(consumerFacade, times(1)).getConsumers();
    }

    @Test
    void getConsumers_returnsConsumerList_whenMultipleConsumers() throws Exception {
        List<Consumer> consumers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            consumers.add(createSampleConsumer((long)i));
        }
        when(consumerFacade.getConsumers()).thenReturn(consumers);

        mockMvc.perform(get("/api/consumers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(consumerFacade, times(1)).getConsumers();
    }

    private List<Consumer> createSampleConsumers() {
        List<Consumer> consumers = new ArrayList<>();
        consumers.add(createSampleConsumer(1L));
        consumers.add(createSampleConsumer(2L));
        return consumers;
    }

    private Consumer createSampleConsumer(Long id) {
        Consumer consumer = new Consumer();
        consumer.setId(id);
        consumer.setName("Consumer " + id);
        consumer.setPhone("+7-900-" + String.format("%04d", id));
        consumer.setEmail("consumer" + id + "@example.com");
        return consumer;
    }
}
