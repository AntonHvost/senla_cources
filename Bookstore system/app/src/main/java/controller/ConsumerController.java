package controller;

import domain.model.impl.Consumer;
import facade.ConsumerFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/consumers")
public class ConsumerController {
    private final ConsumerFacade consumerFacade;

    public ConsumerController(ConsumerFacade consumerFacade) {
        this.consumerFacade = consumerFacade;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Consumer>> getConsumers() {
        return ResponseEntity.ok(consumerFacade.getConsumers());
    }

    public void importConsumer (String filename) {
        consumerFacade.importConsumer(filename);
    }

    public void exportConsumer (String filename) {
        consumerFacade.exportConsumer(filename);
    }
}
