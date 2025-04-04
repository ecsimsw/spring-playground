package com.ecsimsw.transaction.controller

import com.ecsimsw.transaction.config.CANCEL_WEB_HOOK_URL
import com.ecsimsw.transaction.config.SUCCESS_WEB_HOOK_URL
import com.ecsimsw.transaction.service.PayPalService
import com.paypal.api.payments.Payment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PayPalController(
    private val payPalService: PayPalService
) {

    @PostMapping("/api/transaction/pay")
    fun payment(@RequestParam("sum") sum: Double): String {
        val payment: Payment = payPalService.createPayment(
            sum,
            "Payment description",
            SUCCESS_WEB_HOOK_URL,
            CANCEL_WEB_HOOK_URL
        )
        return payment.links
            .firstOrNull { it.rel.equals("approval_url") }
            ?.let { "redirect:${it.href}" }
            ?: throw IllegalArgumentException("approval_url not found in payment links")
    }

    @GetMapping("/api/transaction/success")
    fun successPayment(
        @RequestParam("paymentId") paymentId: String,
        @RequestParam("PayerID") payerId: String
    ): String {
        val payment = payPalService.executePayment(paymentId, payerId)
        if (payment.state == "approved") {
            return "Payment successful"
        }
        return "Payment failed"
    }

    @GetMapping("/api/transaction/cancel")
    fun cancelPayment(): String {
        return "Payment canceled."
    }
}