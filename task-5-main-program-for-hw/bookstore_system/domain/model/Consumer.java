package bookstore_system.domain.model;

public class Consumer {
    private Long id;
    private String name;
    private String phone;
    private String email;

    public Consumer(String name, String phone, String email) {
        this.id++;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
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
