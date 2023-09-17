package com.example.demo.deliveryorder.models.valueobjects

data class ChannelComposition(
    val channelName: String,
    val weight: Weight,
    val numberOfBoxes: Int,
    val address: String,
)
