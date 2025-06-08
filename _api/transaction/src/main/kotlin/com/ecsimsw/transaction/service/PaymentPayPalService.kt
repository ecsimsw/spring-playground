package com.ecsimsw.transaction.service

import com.ecsimsw.transaction.domain.Transaction
import com.ecsimsw.transaction.dto.PaymentInfo
import com.ecsimsw.transaction.dto.PaymentMetaData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.paypal.api.payments.*
import com.paypal.base.rest.APIContext
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class PaymentPayPalService(
    private val apiContext: APIContext,
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
) : PaymentService {

    @Throws(IllegalArgumentException::class)
    override fun create(transaction: Transaction, successUrl: String, cancelUrl: String): PaymentInfo {
        val metadata = PaymentMetaData.from(transaction)
        val payer = Payer().apply {
            this.paymentMethod = "paypal"
            this.payerInfo = PayerInfo()
        }
        val payment = Payment("sale", payer).apply {
            this.setTransactions(listOf(com.paypal.api.payments.Transaction().apply {
                amount = Amount("USD", metadata.amount.toString())
                custom = objectMapper.writeValueAsString(metadata)
            }))
            this.redirectUrls = RedirectUrls().apply {
                this.returnUrl = successUrl
                this.cancelUrl = cancelUrl
            }
        }.create(apiContext)
        val approvalLink = payment.links.firstOrNull { it.rel.equals("approval_url") }
            ?: throw IllegalArgumentException("approval_url not found in payment links")
        return PaymentInfo(payment.id, approvalLink.href)
    }

    @Throws(IllegalArgumentException::class)
    override fun approve(paymentId: String, payerId: String) {
        val payment = Payment().apply {
            this.id = paymentId
        }
        val paymentExecution = PaymentExecution().apply {
            this.payerId = payerId
        }
        val result = payment.execute(apiContext, paymentExecution)
        if (result.state != "approved") {
            throw IllegalArgumentException("Payment is not approved")
        }
    }
}