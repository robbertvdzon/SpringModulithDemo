package com.ahold.technl.delivery.web

import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import java.time.ZonedDateTime

data class DeliveryResponse(
    val id: String,
    val vehicleId: String,
    val address: String,
    val startedAt: ZonedDateTime,
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
)