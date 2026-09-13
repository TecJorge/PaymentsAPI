package pt.codeChallenge.payments.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.codeChallenge.api.CreateTransactionRequest;
import pt.codeChallenge.api.CreateTransactionResponse;
import pt.codeChallenge.api.RetrieveAllTransactionsResponse;
import pt.codeChallenge.payments.entities.Transaction;
import pt.codeChallenge.payments.enums.TransactionFees;
import pt.codeChallenge.payments.mappers.TransactionMapper;
import pt.codeChallenge.payments.repositories.PaymentsTransactionRepository;
import pt.codeChallenge.payments.suppliers.FeeSupplier;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private FeeSupplier feeSupplier;
    private final PaymentsTransactionRepository paymentsTransactionRepository;
    private final TransactionMapper transactionMapper;


    @Scheduled(cron = "0 0 0 * * *")
    public void completeTransactions() {
        paymentsTransactionRepository.markCompleted(LocalDate.now());
    }

    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        validateDates(request);
        TransactionFees fees = feeSupplier.calculateFee(request.getScheduledDate(), request.getAmount());
        Transaction transaction = transactionMapper.toTransaction(request, fees);
        return transactionMapper.toCreateTransactionResponse(paymentsTransactionRepository.save(transaction));

    }

    public RetrieveAllTransactionsResponse retrieveAllTransactions(Long userId){
        List<Transaction> transactionList = paymentsTransactionRepository.findAllByUserId(userId);
        List<pt.codeChallenge.api.Transaction> transactions = transactionMapper.toTransactionList(transactionList);
        return new RetrieveAllTransactionsResponse().transactionList(transactions);
    }

    public pt.codeChallenge.api.Transaction retrieveTransaction(Long userId, Long transactionId){
        Transaction transaction = paymentsTransactionRepository
                .findByUserIdAndTransactionId(userId, transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return transactionMapper.toTransactionDTO(transaction);
    }

    void validateDates(CreateTransactionRequest request) {
        LocalDate today = LocalDate.now();
        if (request.getScheduledDate().isBefore(today)) {
            throw new IllegalArgumentException("Provided scheduledDate must be a present day");
        }
    }
}
