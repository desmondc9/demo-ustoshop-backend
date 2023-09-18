package com.example.demo.deliveryorders.models.valueobjects

data class ChannelComposition(
    val channelName: String,
    val weight: Weight,
    val numberOfBoxes: Int,
    val address: String,
)
