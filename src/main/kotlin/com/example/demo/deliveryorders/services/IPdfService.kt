package com.example.demo.deliveryorders.services

import com.example.demo.deliveryorders.models.DeliveryOrder

interface IPdfService {
    fun generatePdf(deliveryOrder: DeliveryOrder): ByteArray
}
