package com.ecsimsw.transaction.domain

enum class TransactionStatus {
    NONE,
    REQUESTED,
    CREDIT_ADDED,
    APPROVED,
    FAILED,
}