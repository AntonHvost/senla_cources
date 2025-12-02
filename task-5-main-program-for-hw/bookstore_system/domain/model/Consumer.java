package bookstore_system.domain.model;

public class Consumer implements Indedifiable {
    private Long id = 0L;
    private String name;
    private String phone;
    private String email;

    public Consumer(String name, String phone, String email) {
        this.id++;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
