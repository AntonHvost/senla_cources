package domain.model.impl;

import domain.model.Identifiable;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token", schema = "public")
public class RefreshToken implements Identifiable {

    private Long id;
    private User user;
    private String token;
    private Instant expiryDate;
    private boolean revoked;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "token", nullable = false, unique = true)
    public String getToken() {
        return token;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Column(name = "expiry_date", nullable = false)
    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Transient
    public boolean isRevoked() {
        return revoked;
    }

}
