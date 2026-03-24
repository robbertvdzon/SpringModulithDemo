package com.vdzon.springmodulithdemo.invoice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "invoice-service", url = "\${invoice-service.url}")
interface InvoiceClient {

    @PostMapping("/v1/invoices")
    fun sendInvoice(@RequestBody request: InvoiceRequest): InvoiceResponse
}

data class InvoiceRequest(
    val deliveryId: String,
    val address: String
)

data class InvoiceResponse(
    val id: String,
    val sent: Boolean
)