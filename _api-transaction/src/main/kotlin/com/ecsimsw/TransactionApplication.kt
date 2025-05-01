package com.ecsimsw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TransactionApplication

fun main(args: Array<String>) {
    runApplication<TransactionApplication>(*args)
}
