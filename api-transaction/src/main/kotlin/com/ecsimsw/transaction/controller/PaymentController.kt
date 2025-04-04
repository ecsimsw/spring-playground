package com.ecsimsw.transaction.controller

import com.ecsimsw.common.dto.AuthUser
import com.ecsimsw.transaction.config.CANCEL_WEB_HOOK_URL
import com.ecsimsw.transaction.config.SUCCESS_WEB_HOOK_URL
import com.ecsimsw.transaction.dto.TransactionMetaData
import com.ecsimsw.transaction.service.PayPalService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PayPalController(
    private val payPalService: PayPalService
) {

    @PostMapping("/api/transaction/pay")
    fun payment(@RequestParam("sum") amount: Long, user: AuthUser): String {
        val approvalUrl = payPalService.createPayment(
            TransactionMetaData(user.username, amount),
            SUCCESS_WEB_HOOK_URL,
            CANCEL_WEB_HOOK_URL
        )
        return "redirect:$approvalUrl"
    }

    @GetMapping("/api/transaction/success")
    fun successPayment(
        @RequestParam("paymentId") paymentId: String,
        @RequestParam("PayerID") payerId: String
    ): String {
        try {
            payPalService.approve(paymentId, payerId)
            return "Payment successful"
        } catch (e: Exception) {
            e.printStackTrace()
            return "Payment failed"
        }
    }

    @GetMapping("/api/transaction/cancel")
    fun cancelPayment(): String {
        return "Payment canceled."
    }
}