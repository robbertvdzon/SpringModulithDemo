package com.vdzon.springmodulithdemo.delivery

import com.ahold.technl.delivery.web.BusinessSummaryResponse
import com.vdzon.springmodulithdemo.commonmodel.Delivery
import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import com.vdzon.springmodulithdemo.delivery.repository.DeliveryEntity
import com.vdzon.springmodulithdemo.delivery.repository.DeliveryRepository
import com.vdzon.springmodulithdemo.delivery.web.InvoiceDeliveryResponse
import com.vdzon.springmodulithdemo.invoice.InvoiceService
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val invoiceService: InvoiceService,
) {
    private val amsterdamZone = ZoneId.of("Europe/Amsterdam")

    fun createDelivery(vehicleId: String, address: String, startedAt: ZonedDateTime, finishedAt: ZonedDateTime?, status: DeliveryStatus): Delivery {
        validateInput(status, finishedAt)
        return deliveryRepository.save(DeliveryEntity(UUID.randomUUID().toString(), vehicleId, address, startedAt, finishedAt, status)).toDelivery()
    }

    private fun validateInput(status: DeliveryStatus, finishedAt: ZonedDateTime?) {
        if (status == DeliveryStatus.IN_PROGRESS && finishedAt != null) {
            throw IllegalArgumentException("For status IN_PROGRESS the finishedAt field must be null")
        }
        if (status == DeliveryStatus.DELIVERED && finishedAt == null) {
            throw IllegalArgumentException("For status DELIVERED the finishedAt field must be provided")
        }
    }

    fun sendInvoices(deliveryIds: List<String>): List<InvoiceDeliveryResponse> {
        val deliveries = deliveryRepository.findAllById(deliveryIds)
        return deliveries.map { delivery ->
            val response = invoiceService.sendInvoice(delivery.id, delivery.address)
            InvoiceDeliveryResponse(delivery.id, response.id)
        }
    }

    fun getBusinessSummary(): BusinessSummaryResponse {
        val nowAmsterdam = ZonedDateTime.now(amsterdamZone)
        val yesterdayStart = nowAmsterdam.minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val yesterdayEnd = yesterdayStart.plusDays(1).minusNanos(1)
        val deliveries = deliveryRepository.findAllByStartedAtBetweenOrderByStartedAtAsc(yesterdayStart, yesterdayEnd)
        // early return when there are 0 or 1 delivery, since the avarage cannot be calculated then
        if (deliveries.size <= 1) return BusinessSummaryResponse(deliveries.size, 0)
        // create a list of minutes between each delivery
        val totalMinutesDiffsList: List<Long> = deliveries.zipWithNext { a, b ->
            ChronoUnit.MINUTES.between(a.startedAt, b.startedAt)
        }
        // sum the list of minutes between each delivery
        val totalMinutesDiffs = totalMinutesDiffsList.sum()
        // calculate the average
        val averageMinutes = totalMinutesDiffs / (deliveries.size - 1)
        return BusinessSummaryResponse(deliveries.size, averageMinutes)
    }

    fun getAllDeliveries(): List<Delivery> {
        return deliveryRepository.findAll().map { it.toDelivery() }
    }


}