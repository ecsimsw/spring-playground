package com.ecsimsw.transaction.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, String> {

    fun findByPaymentId(paymentId: String): Transaction?
}