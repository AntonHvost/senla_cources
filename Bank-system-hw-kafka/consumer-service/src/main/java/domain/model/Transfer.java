package domain.model;

import domain.enums.TransferStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transfer")
public class Transfer {

    private Long id;
    private Account sourceAccountId;
    private Account destinationAccountId;
    private BigDecimal amount;
    private TransferStatus status;

    public Transfer() {}

    public Transfer(Long id,
                    Account sourceAccountId,
                    Account destinationAccountId,
                    BigDecimal amount,
                    TransferStatus status) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Account getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Account sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Account getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Account destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
