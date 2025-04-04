package com.ecsimsw.transaction.service

import com.ecsimsw.transaction.domain.Transaction
import com.ecsimsw.transaction.dto.PaymentInfo

interface PaymentService {

    fun create(transaction: Transaction, successUrl: String, cancelUrl: String): PaymentInfo
    fun approve(paymentId: String, payerId: String)
}