package com.ecsimsw.transaction.dto

import java.util.*

data class TransactionMetaData(
    val username: String,
    val amount: Int,
    val transactionId: String = UUID.randomUUID().toString()
)
