package com.ahold.technl.delivery.web

import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.ZonedDateTime

data class DeliveryRequest(
    @field:NotBlank
    val vehicleId: String,
    @field:NotBlank
    val address: String,
    @field:NotNull
    val startedAt: ZonedDateTime,
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
)