package pt.codeChallenge.payments.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments_transaction_deleted", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeletedTransaction {

    @Id
    @Column(name = "transactionId", nullable = false)
    private Long transactionId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "scheduledDate", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "fee", precision = 19, scale = 2, nullable = false)
    private BigDecimal fee;

    @Column(name = "totalAmount", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "isComplete", nullable = false)
    private boolean isComplete;
}