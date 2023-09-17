package com.example.demo.deliveryorder.models

import com.example.demo.core.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class DeliveryChannel(
    @Id override val id: String,
    @Version override val version: Long?,
    val name: String,
    val defaultAddress: String
) : Entity {
}
