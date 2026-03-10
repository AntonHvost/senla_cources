package dto;

import domain.model.impl.Consumer;

public class ConsumerDto {
    Long id;
    String name;
    String phone;
    String email;

    public ConsumerDto(Consumer consumer) {
        this.id = consumer.getId();
        this.name = consumer.getName();
        this.phone = consumer.getPhone();
        this.email = consumer.getEmail();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
