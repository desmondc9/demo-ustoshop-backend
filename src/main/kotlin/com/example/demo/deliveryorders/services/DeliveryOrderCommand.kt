package com.example.demo.deliveryorders.services

import com.example.demo.core.DomainCommand
import com.example.demo.deliveryorders.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorders.models.valueobjects.Weight

sealed interface IDeliveryOrderCommand : DomainCommand

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

