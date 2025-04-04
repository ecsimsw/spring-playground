package com.ecsimsw.transaction.dto

import com.ecsimsw.transaction.domain.Audit
import com.ecsimsw.transaction.domain.TransactionStatus
import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
data class TransactionMetaData(
    val username: String,
    val amount: Long,
    val transactionId: String = UUID.randomUUID().toString()
) {
    fun toAudit(status: TransactionStatus): Audit {
        return Audit(transactionId, status, username, amount)
    }

    fun toAudit(status: TransactionStatus, message : String): Audit {
        return Audit(transactionId, status, username, amount, message)
    }
}
