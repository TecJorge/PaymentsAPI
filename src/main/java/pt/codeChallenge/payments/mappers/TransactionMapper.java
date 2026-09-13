package pt.codeChallenge.payments.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pt.codeChallenge.api.CreateTransactionRequest;
import pt.codeChallenge.api.CreateTransactionResponse;
import pt.codeChallenge.api.UpdateTransactionRequest;
import pt.codeChallenge.api.UpdateTransactionResponse;
import pt.codeChallenge.payments.entities.Transaction;
import pt.codeChallenge.payments.enums.TransactionFees;

import java.math.BigDecimal;
import java.util.List;

import static pt.codeChallenge.payments.constants.Constants.FEE_A_FIXED_VALUE;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = ".", source = "request")
    @Mapping(target = "fee", expression = "java(calculateFees(request,fees))")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(request,calculateFees(request,fees)))")
    Transaction toTransaction(CreateTransactionRequest request, TransactionFees fees);

    @Mapping(target = ".", source = "transaction")
    CreateTransactionResponse toCreateTransactionResponse(Transaction transaction);

    List<pt.codeChallenge.api.Transaction> toTransactionList(List<Transaction> transactionList);

    pt.codeChallenge.api.Transaction toTransactionDTO(Transaction transaction);

    @Mapping(target = ".", source = "request")
    @Mapping(target = "fee", expression = "java(calculateFees(request,fees))")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(request,calculateFees(request,fees)))")
    Transaction updateTransaction(@MappingTarget Transaction transaction,UpdateTransactionRequest request, TransactionFees fees);

    UpdateTransactionResponse toUpdateTransactionResponse(Transaction transaction);

    default BigDecimal calculateFees(CreateTransactionRequest request, TransactionFees fees) {
        BigDecimal value = request.getAmount().multiply(fees.getPercentage());
        return TransactionFees.FEE_A.equals(fees) ? value.add(FEE_A_FIXED_VALUE) : value;
    }

    default BigDecimal calculateTotalAmount(CreateTransactionRequest request, BigDecimal fees) {
        return request.getAmount().add(fees);
    }

    default BigDecimal calculateFees(UpdateTransactionRequest request, TransactionFees fees) {
        BigDecimal value = request.getAmount().multiply(fees.getPercentage());
        return TransactionFees.FEE_A.equals(fees) ? value.add(FEE_A_FIXED_VALUE) : value;
    }

    default BigDecimal calculateTotalAmount(UpdateTransactionRequest request, BigDecimal fees) {
        return request.getAmount().add(fees);
    }
}
