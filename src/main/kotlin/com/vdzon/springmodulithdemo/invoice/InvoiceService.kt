package com.vdzon.springmodulithdemo.invoice

import com.vdzon.springmodulithdemo.commonmodel.Invoice
import com.vdzon.springmodulithdemo.invoice.client.InvoiceClient
import com.vdzon.springmodulithdemo.invoice.client.InvoiceRequest
import com.vdzon.springmodulithdemo.invoice.mapper.toInvoice
import org.springframework.stereotype.Service

@Service
class InvoiceService(val invoiceClient: InvoiceClient) {
    fun sendInvoice(deliveryId: String, address: String): Invoice {
        return invoiceClient.sendInvoice(InvoiceRequest(deliveryId, address)).toInvoice()
    }
}


