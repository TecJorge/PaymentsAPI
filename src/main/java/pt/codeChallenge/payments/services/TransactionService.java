package pt.codeChallenge.payments.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.codeChallenge.api.*;
import pt.codeChallenge.payments.entities.DeletedTransaction;
import pt.codeChallenge.payments.entities.Transaction;
import pt.codeChallenge.payments.enums.TransactionFees;
import pt.codeChallenge.payments.mappers.TransactionMapper;
import pt.codeChallenge.payments.repositories.PaymentsTransactionDeletedRepository;
import pt.codeChallenge.payments.repositories.PaymentsTransactionRepository;
import pt.codeChallenge.payments.suppliers.FeeSupplier;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private FeeSupplier feeSupplier;
    private final PaymentsTransactionRepository paymentsTransactionRepository;
    private final PaymentsTransactionDeletedRepository paymentsTransactionDeletedRepository;
    private final TransactionMapper transactionMapper;


    @Scheduled(cron = "0 0 0 * * *")
    public void completeTransactions() {
        paymentsTransactionRepository.markCompleted(LocalDate.now());
    }

    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        validateDates(request.getScheduledDate());
        TransactionFees fees = feeSupplier.calculateFee(request.getScheduledDate(), request.getAmount());
        Transaction transaction = transactionMapper.toTransaction(request, fees);
        return transactionMapper.toCreateTransactionResponse(paymentsTransactionRepository.save(transaction));

    }

    public RetrieveAllTransactionsResponse retrieveAllTransactions(Long userId) {
        List<Transaction> transactionList = paymentsTransactionRepository.findAllByUserId(userId);
        List<pt.codeChallenge.api.Transaction> transactions = transactionMapper.toTransactionList(transactionList);
        return new RetrieveAllTransactionsResponse().transactionList(transactions);
    }

    public pt.codeChallenge.api.Transaction retrieveTransaction(Long userId, Long transactionId) {
        Transaction transaction = paymentsTransactionRepository
                .findByUserIdAndTransactionId(userId, transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return transactionMapper.toTransactionDTO(transaction);
    }

    @Transactional
    public UpdateTransactionResponse updateTransactionResponse(UpdateTransactionRequest request) {
        validateDates(request.getScheduledDate());
        Transaction transaction = paymentsTransactionRepository
                .findByUserIdAndTransactionId(request.getUserId(), request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getIsComplete()) {
            throw new IllegalArgumentException(
                    "Completed transactions cannot be updated"
            );
        }

        TransactionFees fees = feeSupplier.calculateFee(request.getScheduledDate(), request.getAmount());
        Transaction updatedTransaction = transactionMapper.updateTransaction(transaction, request, fees);
        return transactionMapper.toUpdateTransactionResponse(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = paymentsTransactionRepository
                .findByUserIdAndTransactionId(userId, transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        DeletedTransaction deletedTransaction = transactionMapper.toDeletedTransaction(transaction);
        paymentsTransactionDeletedRepository.save(deletedTransaction);
        paymentsTransactionRepository.delete(transaction);
    }

    void validateDates(LocalDate scheduledDate) {
        LocalDate today = LocalDate.now();
        if (scheduledDate.isBefore(today)) {
            throw new IllegalArgumentException("Provided scheduledDate must be a present day");
        }
    }
}
