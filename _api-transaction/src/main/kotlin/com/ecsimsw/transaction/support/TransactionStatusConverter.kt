package com.ecsimsw.transaction.support

import com.ecsimsw.transaction.domain.TransactionStatus
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TransactionStatusConverter : AttributeConverter<TransactionStatus, String> {

    override fun convertToDatabaseColumn(attribute: TransactionStatus): String {
        try {
            return attribute.name
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert service role from db", e)
        }
    }

    override fun convertToEntityAttribute(dbData: String): TransactionStatus {
        try {
            return TransactionStatus.valueOf(dbData)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert service role from entity", e)
        }
    }
}