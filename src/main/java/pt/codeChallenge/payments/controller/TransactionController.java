package pt.codeChallenge.payments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.codeChallenge.api.*;
import pt.codeChallenge.payments.services.TransactionService;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            operationId = "createTransaction",
            summary ="Schedule a transaction",
            responses = {
               @ApiResponse(responseCode = "200",description = "Success",content = {
                       @Content(mediaType = "application/json",schema = @Schema(implementation = CreateTransactionResponse.class))
               })
            }
    )
    @PostMapping("/create")
    public ResponseEntity<CreateTransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        return new ResponseEntity<>(transactionService.createTransaction(request),HttpStatus.OK);
    }

    @Operation(
            operationId = "retrieveTransactions",
            summary ="Retrieve scheduled transactions",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Success",content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = RetrieveAllTransactionsResponse.class))
                    })
            }
    )
    @GetMapping("/retrieve")
    public ResponseEntity<RetrieveAllTransactionsResponse> retrieveTransactions(@Parameter @NotNull Long userId) {
        return new ResponseEntity<>(transactionService.retrieveAllTransactions(userId),HttpStatus.OK);
    }

    @Operation(
            operationId = "retrieveTransaction",
            summary = "Retrieve a transaction by ID",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Success",content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = Transaction.class))
                    })
            }
    )
    @GetMapping()
    public ResponseEntity<Transaction> retrieveTransaction(@Parameter Long userId,
            @Parameter Long transactionId) {

        return new ResponseEntity<>(transactionService.retrieveTransaction(userId,transactionId),HttpStatus.OK);
    }

    @Operation(
            operationId = "updateTransaction",
            summary = "Update a scheduled transaction",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Success",content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = UpdateTransactionResponse.class))
                    })
            }
    )
    @PutMapping("/update")
    public ResponseEntity<UpdateTransactionResponse> updateTransaction(
            @Valid @RequestBody UpdateTransactionRequest request) {
        return new ResponseEntity<>(transactionService.updateTransactionResponse(request),HttpStatus.OK);
    }

    @Operation(
            operationId = "deleteTransaction",
            summary = "Delete a scheduled transaction",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Transaction deleted successfully")
                    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTransaction(
            @RequestParam Long transactionId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}