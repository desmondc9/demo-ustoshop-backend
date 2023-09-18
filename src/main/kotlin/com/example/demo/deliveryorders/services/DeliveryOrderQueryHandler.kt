package com.example.demo.deliveryorders.services

import com.example.demo.core.ViewModel
import com.example.demo.deliveryorders.apis.DownloadPdfQuery
import com.example.demo.deliveryorders.apis.QueryDeliveryOrdersByCustomerId
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
    private val pdfService: PdfService,
) {

    fun handle(query: QueryDeliveryOrdersByCustomerId): DeliveryOrderList {
        val criteria = Query.query(Criteria.where("customerId").`is`(query.customerId))
        val lines = mongoTemplate.find(criteria, DeliveryOrder::class.java).map {
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

    fun handle(downloadPdfQuery: DownloadPdfQuery): ByteArray = try {
        Files.readAllBytes(java.nio.file.Path.of(downloadPdfQuery.path))
    } catch (e: Exception) {
        // regenerate the pdf file cause the app is deployed in a container environment,
        // and every time the container is restarted, the pdf file is gone
        // will not do it when in production environment, because there will be volumes and persistent storages
        mongoTemplate.findById(downloadPdfQuery.deliveryOrderId, DeliveryOrder::class.java)?.let {
            pdfService.generatePdf(it)
        } ?: throw Exception("DeliveryOrder not found for generating pdf: ${downloadPdfQuery.deliveryOrderId}")

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
