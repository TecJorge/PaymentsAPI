package pt.codeChallenge.payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pt.codeChallenge.payments.entities.DeletedTransaction;


@Repository
public interface PaymentsTransactionDeletedRepository extends JpaRepository<DeletedTransaction, Long>, JpaSpecificationExecutor<DeletedTransaction> {

}
