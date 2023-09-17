package com.example.demo.deliveryorder

import com.example.demo.deliveryorder.models.DeliveryChannel
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.stereotype.Service

@Service
class DeliveryOrderQueryHandler(
    private val mongoTemplate: MongoTemplate,
) {
    fun queryAllDeliveryChannels(): List<DeliveryChannel> {
        val result = mongoTemplate.findAll(DeliveryChannel::class.java)
        return result
    }
}

