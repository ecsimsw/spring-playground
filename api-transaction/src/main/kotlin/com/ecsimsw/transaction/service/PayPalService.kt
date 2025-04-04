package com.ecsimsw.transaction.service

import com.paypal.api.payments.*
import com.paypal.base.rest.APIContext
import org.springframework.stereotype.Service

@Service
class PayPalService(
    private val apiContext: APIContext
) {

    fun createPayment(total: Double, description: String, successUrl: String, cancelUrl: String): Payment {
        val transaction = Transaction().apply {
            this.amount = Amount("USD", String.format("%.2f", total))
            this.description = description
        }

        val payer = Payer().apply {
            this.paymentMethod = "paypal"
            this.payerInfo = PayerInfo().apply {
                email = "user@example.com"
                firstName = "John"
                lastName = "Doe"
            }
        }

        val payment = Payment("sale", payer).apply {
            this.setTransactions(listOf(transaction))
            this.redirectUrls = RedirectUrls().apply {
                this.returnUrl = successUrl
                this.cancelUrl = cancelUrl
            }
        }
        return payment.create(apiContext)
    }

    fun executePayment(paymentId: String, payerId: String): Payment {
        val payment = Payment()
        payment.setId(paymentId)
        val paymentExecution = PaymentExecution()
        paymentExecution.setPayerId(payerId)
        return payment.execute(apiContext, paymentExecution)
    }
}