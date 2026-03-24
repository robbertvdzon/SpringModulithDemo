package com.ahold.technl.delivery.web

data class BusinessSummaryResponse(
    val deliveries: Int,
    val averageMinutesBetweenDeliveryStart: Long
)
