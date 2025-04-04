package com.ecsimsw.transaction.service

import com.ecsimsw.common.client.UserClient
import com.ecsimsw.transaction.dto.TransactionMetaData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.paypal.api.payments.*
import com.paypal.base.rest.APIContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PayPalService(
    private val apiContext: APIContext,
    private val userClient: UserClient,
    private val auditService: AuditService,
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
) {

    fun createPayment(
        metaData: TransactionMetaData,
        successUrl: String,
        cancelUrl: String
    ): String {
        val payer = Payer().apply {
            this.paymentMethod = "paypal"
            this.payerInfo = PayerInfo()
        }
        val payment = Payment("sale", payer).apply {
            val transaction = Transaction().apply {
                amount = Amount("USD", metaData.amount.toString())
                custom = objectMapper.writeValueAsString(metaData)
            }
            this.setTransactions(listOf(transaction))
            redirectUrls = RedirectUrls().apply {
                this.returnUrl = successUrl
                this.cancelUrl = cancelUrl
            }
        }
        val paymentUrl = payment.create(apiContext).links
            .firstOrNull { it.rel.equals("approval_url") }
            ?.href
            ?: throw IllegalArgumentException("approval_url not found in payment links")

        auditService.requested(metaData)
        return paymentUrl
    }

    fun approve(paymentId: String, payerId: String) {
        val getPayment = Payment.get(apiContext, paymentId)
        val transaction = getPayment.transactions.first()
        val metaData = objectMapper.readValue(transaction.custom, TransactionMetaData::class.java)
        addCredit(metaData)

        val executePayment = executePayment(paymentId, payerId)
        if (isFailed(executePayment)) {
            auditService.failed(metaData, executePayment.failureReason)
            userClient.rollbackCreditAddition(metaData.username, metaData.amount)
            throw IllegalArgumentException("Failed to payment")
        }
        auditService.approved(metaData)
    }

    private fun addCredit(metaData: TransactionMetaData) {
        val creditAddedResponse = userClient.addCredit(metaData.username, metaData.amount)
        if (creditAddedResponse.statusCode != HttpStatus.OK) {
            auditService.failed(metaData, "Failed to add credit")
            throw IllegalArgumentException("Failed to add credit")
        }
        auditService.creditAdded(metaData)
    }

    private fun executePayment(paymentId: String, payerId: String): Payment {
        val payment = Payment().apply {
            this.id = paymentId
        }
        val paymentExecution = PaymentExecution().apply {
            this.payerId = payerId
        }
        return payment.execute(apiContext, paymentExecution)
    }

    private fun isFailed(payment: Payment): Boolean {
        return payment.state != "approved";
    }
}