package com.example.demo.deliveryorder

import com.example.demo.deliveryorder.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorder.models.valueobjects.Weight

sealed interface IDeliveryOrderCommand

data class ImportRawDeliveryData(
    val billOfLadingNumber: String,
    val customerId: String,
    val totalWeight: Weight,
    val totalNumberOfBoxes: Int,
    val channelCompositions: List<ChannelComposition>,
) : IDeliveryOrderCommand

data class GenerateDeliveryOrder(
    val rawDeliveryDataId: String,
) : IDeliveryOrderCommand

data class CreateDeliveryChannel(
    val name: String,
    val defaultAddress: String,
) : IDeliveryOrderCommand


data class UpdateDeliveryChannel(
    val id: String,
    val expectedPreviousVersion: Long,
    val name: String,
    val defaultAddress: String,
) : IDeliveryOrderCommand


data class DeleteDeliveryChannel(
    val id: String,
) : IDeliveryOrderCommand

