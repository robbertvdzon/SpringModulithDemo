package com.vdzon.springmodulithdemo.delivery.web

import com.ahold.technl.delivery.web.BusinessSummaryResponse
import com.ahold.technl.delivery.web.DeliveryRequest
import com.ahold.technl.delivery.web.DeliveryResponse
import com.vdzon.springmodulithdemo.commonmodel.Delivery
import com.vdzon.springmodulithdemo.delivery.DeliveryService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/deliveries")
class DeliveryController(
    val deliveryService: DeliveryService
) {
    private val log = LoggerFactory.getLogger(DeliveryController::class.java)

    @PostMapping
    fun createDelivery(@Valid @RequestBody request: DeliveryRequest): DeliveryResponse {
        val delivery = deliveryService.createDelivery(request.vehicleId, request.address, request.startedAt, request.finishedAt, request.status)

        return DeliveryResponse(
            id = delivery.id,
            vehicleId = delivery.vehicleId,
            address = delivery.address,
            startedAt = delivery.startedAt,
            finishedAt = delivery.finishedAt,
            status = delivery.status
        )
    }

    @GetMapping
    fun getAllDeliveries(): List<DeliveryResponse> {
        val allDeliveries: List<Delivery> = deliveryService.getAllDeliveries()
        return allDeliveries.map{it.toResponse()}
    }

    @GetMapping("/business-summary")
    fun getBusinessSummary(): BusinessSummaryResponse {
        return deliveryService.getBusinessSummary()
    }

    @PostMapping("/invoice")
    fun sendInvoices(@RequestBody request: InvoiceDeliveryRequest): List<InvoiceDeliveryResponse> {
        return deliveryService.sendInvoices(request.deliveryIds)
    }

}

private fun Delivery.toResponse(): DeliveryResponse {
    return DeliveryResponse(
        id = id,
        vehicleId = vehicleId,
        address = address,
        startedAt = startedAt,
        finishedAt = finishedAt,
        status = status
    )
}

data class InvoiceDeliveryRequest(
    val deliveryIds: List<String>
)

data class InvoiceDeliveryResponse(
    val deliveryId: String,
    val invoiceId: String
)

