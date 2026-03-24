package com.vdzon.springmodulithdemo.invoice.mapper

import com.vdzon.springmodulithdemo.commonmodel.Invoice
import com.vdzon.springmodulithdemo.invoice.client.InvoiceResponse

fun InvoiceResponse.toInvoice(): Invoice {
    return Invoice(id, sent)
}