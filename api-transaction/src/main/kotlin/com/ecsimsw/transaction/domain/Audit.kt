package com.ecsimsw.transaction.domain

import com.ecsimsw.transaction.support.TransactionStatusConverter
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime

@Getter
@Entity
class Audit(
    private val transactionId: String = "",
    @field:Convert(converter = TransactionStatusConverter::class)
    private val status: TransactionStatus = TransactionStatus.NONE,
    private val message: String = ""
) {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private val id: Long? = null
    private val timestamp: LocalDateTime = LocalDateTime.now()
}
