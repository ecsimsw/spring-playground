package com.ecsimsw.transaction.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AuditRepository : JpaRepository<Audit, Long> {
}