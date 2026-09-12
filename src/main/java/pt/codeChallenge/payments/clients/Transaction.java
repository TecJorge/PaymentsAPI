package pt.codeChallenge.payments.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", nullable = false)
    private Long transactionId;
    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;
    @Column(name = "scheduledDate", nullable = false)
    private LocalDate scheduledDate;
    @Column(name = "fee", precision = 19, scale = 2, nullable = false)
    private BigDecimal fee;
    @Column(name = "totalAmount", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount;
}