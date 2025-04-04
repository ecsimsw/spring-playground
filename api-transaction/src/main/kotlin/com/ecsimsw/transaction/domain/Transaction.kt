package com.ecsimsw.transaction.domain

import com.ecsimsw.transaction.support.TransactionStatusConverter
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime
import java.util.UUID

@Getter
@Table(name = "transactions")
@Entity
class Transaction(
    @Id
    val id: String = UUID.randomUUID().toString(),
    @field:Convert(converter = TransactionStatusConverter::class)
    var status: TransactionStatus = TransactionStatus.NONE,
    val username: String = "",
    var paymentId: String = "",
    val amount: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    fun paymentRequested(paymentId: String) : Audit {
        this.status = TransactionStatus.REQUESTED
        this.paymentId = paymentId
        this.updatedAt = LocalDateTime.now()
        return Audit(id, status)
    }

    fun failed(reason: String) : Audit {
        this.status = TransactionStatus.FAILED
        this.updatedAt = LocalDateTime.now()
        return Audit(id, status, reason)
    }

    fun paymentApproved() : Audit {
        this.status = TransactionStatus.APPROVED
        this.updatedAt = LocalDateTime.now()
        return Audit(id, status)
    }

    fun isStatus(status: TransactionStatus) = this.status == status
}
