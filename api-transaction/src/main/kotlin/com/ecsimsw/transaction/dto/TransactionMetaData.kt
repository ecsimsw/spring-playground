package com.ecsimsw.transaction.dto

import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
data class TransactionMetaData(
    val username: String,
    val amount: Long,
    val transactionId: String = UUID.randomUUID().toString()
)
