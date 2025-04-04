package com.ecsimsw.transaction.service

import com.paypal.api.payments.*
import com.paypal.base.rest.APIContext
import com.paypal.base.rest.PayPalRESTException
import org.springframework.stereotype.Service

@Service
class PayPalService(
    private val apiContext: APIContext
) {

    @Throws(PayPalRESTException::class)
    fun createPayment(total: Double, currency: String?, description: String?, successUrl: String?, cancelUrl: String?,): Payment {
        val amount = Amount(currency, String.format("%.2f", total))
        val transaction = Transaction()
        transaction.setDescription(description)
        transaction.setAmount(amount)

        val payer = Payer()
        payer.setPaymentMethod("paypal")

        val redirectUrls = RedirectUrls()
        redirectUrls.setReturnUrl(successUrl)
        redirectUrls.setCancelUrl(cancelUrl)

        val payment = Payment("sale", payer)
        payment.setTransactions(listOf(transaction))
        payment.setRedirectUrls(redirectUrls)
        return payment.create(apiContext)
    }

    @Throws(PayPalRESTException::class)
    fun executePayment(paymentId: String?, payerId: String?): Payment {
        val payment = Payment()
        payment.setId(paymentId)
        val paymentExecution = PaymentExecution()
        paymentExecution.setPayerId(payerId)
        return payment.execute(apiContext, paymentExecution)
    }
}