package com.example.demo.deliveryorder.models

import com.example.demo.core.Entity
import com.example.demo.deliveryorder.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorder.models.valueobjects.Weight
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class RawDeliveryData(
    @Id override val id: String,
    @Version override val version: Long?,
    val billOfLadingNumber: String,
    val totalWeight: Weight,
    val totalNumberOfBoxes: Int,
    val channelCompositions: List<ChannelComposition>,
    val customerId: String,
) : Entity
