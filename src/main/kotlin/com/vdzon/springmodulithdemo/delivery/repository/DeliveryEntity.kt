package com.vdzon.springmodulithdemo.delivery.repository

import com.vdzon.springmodulithdemo.commonmodel.Delivery
import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "delivery")
data class DeliveryEntity(
    @Id
    val id: String,
    val vehicleId: String,
    val address: String,
    val startedAt: ZonedDateTime,
    val finishedAt: ZonedDateTime?,
    @Enumerated(EnumType.STRING)
    val status: DeliveryStatus
) {
    fun toDelivery(): Delivery {
        return Delivery(id, vehicleId, address, startedAt, finishedAt, status)
    }
}