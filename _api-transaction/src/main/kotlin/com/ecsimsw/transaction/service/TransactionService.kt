package com.ecsimsw.transaction.service

import com.ecsimsw.transaction.domain.AuditRepository
import com.ecsimsw.transaction.domain.Transaction
import com.ecsimsw.transaction.domain.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val auditRepository: AuditRepository,
) {

    @Transactional
    open fun create(username: String, amount: Long) : Transaction{
        val transaction = Transaction(username = username, amount = amount)
        transactionRepository.save(transaction)
        return transaction
    }

    @Transactional
    open fun paymentRequested(transaction: Transaction, paymentId: String) {
        val audit = transaction.paymentRequested(paymentId)
        transactionRepository.save(transaction)
        auditRepository.save(audit)
    }

    @Transactional
    open fun approved(transaction: Transaction) {
        val audit = transaction.paymentApproved()
        transactionRepository.save(transaction)
        auditRepository.save(audit)
    }

    @Transactional
    open fun failed(transaction: Transaction, reason: String) {
        val audit = transaction.failed(reason)
        transactionRepository.save(transaction)
        auditRepository.save(audit)
    }

    @Transactional(readOnly = true)
    open fun findByPaymentId(paymentId: String): Transaction {
        return transactionRepository.findByPaymentId(paymentId) ?: throw NoSuchElementException()
    }
}