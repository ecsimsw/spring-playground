package com.ecsimsw.transaction.config

import com.paypal.base.rest.APIContext
import com.paypal.base.rest.OAuthTokenCredential
import com.paypal.base.rest.PayPalRESTException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val SUCCESS_WEB_HOOK_URL: String = "http://localhost:8080/api/transaction/approve"
const val CANCEL_WEB_HOOK_URL: String = "http://localhost:8080/api/transaction/cancel"

@Configuration
open class PayPalConfig {

    @Value("\${paypal.client.id}")
    private val clientId: String? = null

    @Value("\${paypal.client.secret}")
    private val clientSecret: String? = null

    @Value("\${paypal.mode}")
    private val mode: String? = null

    @Bean
    open fun paypalSdkConfig(): Map<String, String?> {
        return mapOf(Pair("mode", mode))
    }

    @Bean
    open fun oAuthTokenCredential(): OAuthTokenCredential {
        return OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig())
    }

    @Bean
    @Throws(PayPalRESTException::class)
    open fun apiContext(): APIContext {
        return APIContext(oAuthTokenCredential().accessToken).apply {
            configurationMap = paypalSdkConfig()
        }
    }
}
