package com.ecsimsw.transaction.service

import com.ecsimsw.transaction.dto.TransactionMetaData
import com.fasterxml.jackson.databind.ObjectMapper
import com.paypal.api.payments.*
import com.paypal.base.rest.APIContext
import org.springframework.stereotype.Service

@Service
class PayPalService(
    private val apiContext: APIContext,
    private val objectMapper: ObjectMapper
) {

    fun createPayment(
        metaData: TransactionMetaData,
        successUrl: String,
        cancelUrl: String
    ): String {
        val transaction = Transaction().apply {
            this.amount = Amount("USD", metaData.amount.toString())
            this.custom = objectMapper.writeValueAsString(metaData)
        }
        val payer = Payer().apply {
            this.paymentMethod = "paypal"
            this.payerInfo = PayerInfo()
        }
        val payment = Payment("sale", payer).apply {
            this.setTransactions(listOf(transaction))
            this.redirectUrls = RedirectUrls().apply {
                this.returnUrl = successUrl
                this.cancelUrl = cancelUrl
            }
        }
        return payment.create(apiContext).links
            .firstOrNull { it.rel.equals("approval_url") }
            ?.href
            ?: throw IllegalArgumentException("approval_url not found in payment links")
    }

    fun executePayment(paymentId: String, payerId: String): Payment {
        val payment = Payment().apply {
            this.id = paymentId
        }
        val paymentExecution = PaymentExecution().apply {
            this.payerId = payerId
        }
        return payment.execute(apiContext, paymentExecution)
    }
}