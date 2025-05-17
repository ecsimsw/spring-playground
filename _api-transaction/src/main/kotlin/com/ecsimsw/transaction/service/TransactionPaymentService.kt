package com.ecsimsw.transaction.service

import com.ecsimsw.common.client.AccountClient
import com.ecsimsw.transaction.domain.Transaction
import com.ecsimsw.transaction.domain.TransactionStatus
import com.ecsimsw.transaction.support.MemLock
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
open class TransactionPaymentService(
    private val accountClient: AccountClient,
    private val paymentService: PaymentService,
    private val transactionService: TransactionService
) {
    open fun create(username: String, amount: Long, successUrl: String, cancelUrl: String): String {
        val transaction = transactionService.create(username, amount)
        val payment = paymentService.create(transaction, successUrl, cancelUrl)
        transactionService.paymentRequested(transaction, payment.id)
        return payment.url
    }

    open fun approve(paymentId: String, payerId: String) {
        try {
            MemLock.tryLock(paymentId, 500)
            val transaction = getRequestedTransaction(paymentId)
            addCredit(transaction)
            try {
                paymentService.approve(paymentId, payerId)
                transactionService.approved(transaction)
            } catch (e: Exception) {
                transactionService.failed(transaction, "Failed to payment")
                rollbackCredit(transaction)
                throw IllegalArgumentException("Failed to payment")
            }
        } finally {
            MemLock.unlock(paymentId)
        }
    }

    private fun getRequestedTransaction(paymentId: String): Transaction {
        val transaction = transactionService.findByPaymentId(paymentId)
        if (!transaction.isStatus(TransactionStatus.REQUESTED)) {
            throw IllegalArgumentException("Not a valid transaction")
        }
        return transaction
    }

    private fun addCredit(transaction: Transaction) {
        val creditAddedResponse = accountClient.addCredit(transaction.username, transaction.amount)
        if (creditAddedResponse.statusCode != HttpStatus.OK) {
            transactionService.failed(transaction, "Failed to add credit")
            throw IllegalArgumentException("Failed to add credit")
        }
    }

    private fun rollbackCredit(transaction: Transaction) {
        accountClient.rollbackCreditAddition(transaction.username, transaction.amount)
    }
}