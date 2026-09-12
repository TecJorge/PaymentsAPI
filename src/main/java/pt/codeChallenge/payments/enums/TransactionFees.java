package pt.codeChallenge.payments.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum TransactionFees {
    FEE_A(new BigDecimal("0.03")),
    FEE_B(new BigDecimal("0.09")),
    FEE_C_11_20(new BigDecimal("0.082")),
    FEE_C_21_30(new BigDecimal("0.069")),
    FEE_C_31_40(new BigDecimal("0.047")),
    FEE_C_40(new BigDecimal("0.017"));
    private final BigDecimal percentage;
}
