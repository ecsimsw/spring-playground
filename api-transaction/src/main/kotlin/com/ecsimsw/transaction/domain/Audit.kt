package com.ecsimsw.transaction.domain

import com.ecsimsw.transaction.support.TransactionStatusConverter
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime

@Getter
@Entity
class Audit @JvmOverloads constructor(
    private val transactionId: String = "",
    @field:Convert(converter = TransactionStatusConverter::class)
    private val status: TransactionStatus = TransactionStatus.NONE,
    private val username: String = "",
    private val amount: Long = 0L,
    private val message: String = ""
) {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private val id: Long? = null
    private val timestamp: LocalDateTime = LocalDateTime.now()
}
