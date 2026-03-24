package com.vdzon.springmodulithdemo.delivery.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface DeliveryRepository : JpaRepository<DeliveryEntity, String> {
    fun findAllByStartedAtBetweenOrderByStartedAtAsc(start: ZonedDateTime, end: ZonedDateTime): List<DeliveryEntity>
}
