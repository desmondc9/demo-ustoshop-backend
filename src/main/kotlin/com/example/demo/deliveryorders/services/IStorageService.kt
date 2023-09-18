package com.example.demo.deliveryorders.services

import com.example.demo.deliveryorders.models.DeliveryOrder

interface IStorageService {
    fun save(content: ByteArray, deliveryOrder: DeliveryOrder)
    fun generatePathFor(deliveryOrder: DeliveryOrder): String
}
