package com.ecsimsw.transaction.controller

import com.ecsimsw.transaction.service.PayPalService
import com.paypal.api.payments.Payment
import com.paypal.base.rest.PayPalRESTException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

const val SUCCESS_URL: String = "http://localhost:8080/api/transaction/success"
const val CANCEL_URL: String = "http://localhost:8080/api/transaction/cancel"

@RestController
class PayPalController(
    private val payPalService: PayPalService
) {

    @PostMapping("/api/transaction/pay")
    fun payment(@RequestParam("sum") sum: Double): String {
        val payment: Payment = payPalService.createPayment(
            sum,
            "USD",
            "Payment description",
            SUCCESS_URL,
            CANCEL_URL
        )
        for (link in payment.links) {
            if (link.rel.equals("approval_url")) {
                return "redirect:" + link.href
            }
        }
        throw IllegalArgumentException()
    }

    @GetMapping("/api/transaction/success")
    fun successPayment(
        @RequestParam("paymentId") paymentId: String?,
        @RequestParam("PayerID") payerId: String?
    ) : String {
        try {
            val payment = payPalService.executePayment(paymentId, payerId)
            if (payment.state == "approved") {
                return "Payment successful"
            }
        } catch (e: PayPalRESTException) {
            e.printStackTrace()
        }
        return "Payment failed"
    }

    @GetMapping("/api/transaction/cancel")
    fun cancelPayment(): String {
        return "Payment canceled."
    }
}