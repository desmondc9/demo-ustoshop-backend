package com.example.demo.deliveryorders.models

import com.example.demo.deliveryorders.models.valueobjects.Weight
import com.example.demo.core.Aggregate
import com.example.demo.deliveryorders.models.valueobjects.DeliveryOrderType
import com.example.demo.deliveryorders.models.valueobjects.PaymentType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class DeliveryOrder(
    @Id override val id: String,
    @Version override val version: Long?,
    val customerId: String,
    val issuer: String,
    val issuedDate: LocalDate,
    val airWaybillNumber: String,
    val numberOfPackages: Int,
    val addressForDeliveryTo: String,
    val arrivalDate: LocalDate,
    val ourRefNumber: String,
    val weight: Weight,
    val fromRawDeliveryDataId: String,
    val carrier: String = "",
    val location: String = "",
    val originalPort: String = "",
    val destinationPort: String = "",
    val freeTimeExpired: LocalDate? = null,
    val houseNumber: String = "",
    val entryBillOfLadingNumber: String = "",
    val customsRefNumber: String = "",
    val route: String = "",
    val descriptions: String = "",
    val type: DeliveryOrderType = DeliveryOrderType.INLAND_FRIGHT,
    val paymentType: PaymentType = PaymentType.PREPAID,
    val storagePath: String = "",
) : Aggregate
