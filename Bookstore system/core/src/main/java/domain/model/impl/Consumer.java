package domain.model.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import domain.model.Identifiable;

import javax.persistence.*;

@JsonAutoDetect
@Entity
@Table(name = "consumer")
public class Consumer implements Identifiable {
    private Long id;
    private String name;
    private String phone;
    private String email;

    public Consumer(){}

    public Consumer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumer_seq")
    @SequenceGenerator(name = "consumer_seq",
            sequenceName = "consumer_id_seq",
            allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
