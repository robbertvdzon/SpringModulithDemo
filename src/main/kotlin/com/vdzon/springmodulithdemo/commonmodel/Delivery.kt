package com.vdzon.springmodulithdemo.commonmodel

import java.time.ZonedDateTime

enum class DeliveryStatus {
    IN_PROGRESS,
    DELIVERED
}

data class Delivery(
    val id: String,
    val vehicleId: String,
    val address: String,
    val startedAt: ZonedDateTime,
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
)