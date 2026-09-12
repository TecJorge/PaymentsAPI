package pt.codeChallenge.payments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

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

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
    public ResponseEntity<RetrieveAllTransactionsResponse> retrieveTransactions() {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> retrieveTransaction(
            @PathVariable Long transactionId) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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