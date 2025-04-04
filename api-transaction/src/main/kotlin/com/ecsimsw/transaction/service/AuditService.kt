package com.ecsimsw.transaction.service

import com.ecsimsw.transaction.domain.AuditRepository
import com.ecsimsw.transaction.domain.TransactionStatus
import com.ecsimsw.transaction.dto.TransactionMetaData
import org.springframework.stereotype.Service

@Service
class AuditService(
    private val auditRepository: AuditRepository
) {

    fun requested(metaData: TransactionMetaData) {
        val paymentRequested = metaData.toAudit(TransactionStatus.REQUESTED)
        auditRepository.save(paymentRequested)
    }

    fun creditAdded(metaData: TransactionMetaData) {
        val creditAdded = metaData.toAudit(TransactionStatus.CREDIT_ADDED)
        auditRepository.save(creditAdded)
    }

    fun failed(metaData: TransactionMetaData, reason: String) {
        val paymentRequested = metaData.toAudit(TransactionStatus.FAILED, reason)
        auditRepository.save(paymentRequested)
    }

    fun approved(metaData: TransactionMetaData) {
        val creditAdded = metaData.toAudit(TransactionStatus.APPROVED)
        auditRepository.save(creditAdded)
    }
}