package com.example.demo.deliveryorders.services

import com.example.demo.core.ViewModel
import com.example.demo.deliveryorders.models.DeliveryChannel
import com.example.demo.deliveryorders.models.DeliveryOrder
import com.example.demo.deliveryorders.models.valueobjects.Weight
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.time.format.DateTimeFormatter

@Service
class DeliveryOrderQueryHandler(
    private val mongoTemplate: MongoTemplate,
) {

    fun queryDeliveryOrdersByCustomerId(customerId: String): DeliveryOrderList {
        val query = Query.query(Criteria.where("customerId").`is`(customerId))
        val lines = mongoTemplate.find(query, DeliveryOrder::class.java).map {
            DeliveryOrderLine(
                id = it.id,
                version = it.version ?: throw Exception("DeliveryOrder version is null: ${it.id}"),
                issuer = it.issuer,
                issuedDate = it.issuedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                airWaybillNumber = it.airWaybillNumber,
                numberOfPackages = it.numberOfPackages,
                addressForDeliveryTo = it.addressForDeliveryTo,
                arrivalDate = it.arrivalDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                ourRefNumber = it.ourRefNumber,
                weight = it.weight,
                fromRawDeliveryDataId = it.fromRawDeliveryDataId,
                carrier = it.carrier,
                location = it.location,
                originalPort = it.originalPort,
                destinationPort = it.destinationPort,
                freeTimeExpired = it.freeTimeExpired?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: "",
                houseNumber = it.houseNumber,
                entryBillOfLadingNumber = it.entryBillOfLadingNumber,
                customsRefNumber = it.customsRefNumber,
                route = it.route,
                descriptions = it.descriptions,
                type = it.type.name,
                paymentType = it.paymentType.name,
                storagePath = it.storagePath,
            )
        }
        return DeliveryOrderList(lines)
    }

    fun downloadPdf(path: String): ByteArray {
        val result = Files.readAllBytes(java.nio.file.Path.of(path))
        return result
    }

    fun queryAllDeliveryChannels(): DeliveryChannelListView {
        val lines = mongoTemplate.findAll(DeliveryChannel::class.java)
            .map {
                DeliveryChannelLine(
                    id = it.id,
                    version = it.version ?: throw Exception("DeliveryChannel version is null: ${it.id}"),
                    name = it.name,
                    defaultAddress = it.defaultAddress,
                )
            }
        val result = DeliveryChannelListView(lines)
        return result
    }
}

data class DeliveryOrderList(
    val data: List<DeliveryOrderLine>,
) : ViewModel

data class DeliveryOrderLine(
    val id: String,
    val version: Long,
    val issuer: String,
    val issuedDate: String,
    val airWaybillNumber: String,
    val numberOfPackages: Int,
    val addressForDeliveryTo: String,
    val arrivalDate: String,
    val ourRefNumber: String,
    val weight: Weight,
    val fromRawDeliveryDataId: String,
    val carrier: String,
    val location: String,
    val originalPort: String,
    val destinationPort: String,
    val freeTimeExpired: String,
    val houseNumber: String,
    val entryBillOfLadingNumber: String,
    val customsRefNumber: String,
    val route: String,
    val descriptions: String,
    val type: String,
    val paymentType: String,
    val storagePath: String,
)


data class DeliveryChannelListView(
    val data: List<DeliveryChannelLine>,
) : ViewModel

data class DeliveryChannelLine(
    val id: String,
    val version: Long,
    val name: String,
    val defaultAddress: String,
)
