package com.ecsimsw.transaction.dto

import com.ecsimsw.transaction.domain.Transaction
import lombok.NoArgsConstructor

@NoArgsConstructor
data class PaymentMetaData(
    val transactionId: String,
    val username: String,
    val amount: Long
) {
    companion object {
        fun from(transaction: Transaction): PaymentMetaData {
            return PaymentMetaData(transaction.id, transaction.username, transaction.amount)
        }
    }
}
