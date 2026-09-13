package pt.codeChallenge.payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.codeChallenge.payments.entities.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsTransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Modifying
    @Query("""
                UPDATE Transaction t
                SET t.isComplete = true
                WHERE t.isComplete = false
                  AND t.scheduledDate <= :today
            """)
    int markCompleted(LocalDate today);


    List<Transaction> findAllByUserId(Long userId);

    Optional<Transaction> findByUserIdAndTransactionId(Long userId, Long transactionId);
}
