package com.example.demo.deliveryorders.services

import com.example.demo.deliveryorders.models.DeliveryChannel
import com.example.demo.deliveryorders.models.DeliveryOrder
import com.example.demo.deliveryorders.models.RawDeliveryData
import com.example.demo.deliveryorders.repositories.IDeliveryChannelRepository
import com.example.demo.deliveryorders.repositories.IDeliveryOrderRepository
import com.example.demo.deliveryorders.repositories.IRawDeliveryDataRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(rollbackFor = [Exception::class])
class DeliveryOrderCommandHandler(
    val deliveryOrderRepository: IDeliveryOrderRepository,
    val rawDeliveryDataRepository: IRawDeliveryDataRepository,
    val deliveryChannelRepository: IDeliveryChannelRepository,
    val pdfService: IPdfService,
    val storageService: IStorageService,
    val airWaybillService: IAirWaybillService,
) {
    fun handle(cmd: ImportRawDeliveryData): Unit {
        val rawDeliveryData = RawDeliveryData(
            id = ObjectId().toString(),
            version = null,
            billOfLadingNumber = cmd.billOfLadingNumber,
            customerId = cmd.customerId,
            totalWeight = cmd.totalWeight,
            totalNumberOfBoxes = cmd.totalNumberOfBoxes,
            channelCompositions = cmd.channelCompositions,
        )
        rawDeliveryDataRepository.save(rawDeliveryData)

        // Can be moved to async job, but for demo purpose, I just call it synchronously
        handle(GenerateDeliveryOrder(rawDeliveryData.id))
    }

    fun handle(cmd: GenerateDeliveryOrder): Unit {
        val ustoshopAddress = """USTOSHOP.COM INC
                                     |3245 Story Road West
                                     |Irving, TX 75038""".trimMargin()
        val today = LocalDate.now()
        rawDeliveryDataRepository.findById(cmd.rawDeliveryDataId)
            .map { rawDeliveryData ->
                val pickOrder = generatePickOrder(today, rawDeliveryData, ustoshopAddress)
                val deliveryOrders = generateDeliveryOrders(today, rawDeliveryData)
                val allOrders = (deliveryOrders + pickOrder)
                    .map { deliveryOrder ->
                        val pdf = pdfService.generatePdf(deliveryOrder)
                        storageService.save(content = pdf, deliveryOrder = deliveryOrder)
                        deliveryOrder.copy(storagePath = storageService.generatePathFor(deliveryOrder))
                    }
                deliveryOrderRepository.saveAll(allOrders)
            }.orElseThrow { RuntimeException("Raw delivery data not found") }
    }

    private fun generateDeliveryOrders(
        date: LocalDate,
        rawDeliveryData: RawDeliveryData,
    ) = rawDeliveryData.channelCompositions.map { channelComposition ->
        DeliveryOrder(
            id = ObjectId().toString(),
            version = null,
            issuer = channelComposition.channelName,
            airWaybillNumber = rawDeliveryData.billOfLadingNumber,
            issuedDate = date,
            numberOfPackages = channelComposition.numberOfBoxes,
            addressForDeliveryTo = channelComposition.address,
            arrivalDate = date,
            ourRefNumber = "11694318890095",
            weight = channelComposition.weight,
            fromRawDeliveryDataId = rawDeliveryData.id,
            storagePath = "not saved yet",
            customerId = rawDeliveryData.customerId,
        )
    }

    fun handle(cmd: DeleteDeliveryOrder) {
        deliveryOrderRepository.deleteById(cmd.id)
    }

    private fun generatePickOrder(
        date: LocalDate,
        rawDeliveryData: RawDeliveryData,
        ustoshopAddress: String,
    ) = DeliveryOrder(
        id = ObjectId().toString(),
        version = null,
        issuer = ustoshopAddress,
        airWaybillNumber = rawDeliveryData.billOfLadingNumber,
        issuedDate = date,
        numberOfPackages = rawDeliveryData.totalNumberOfBoxes,
        addressForDeliveryTo = ustoshopAddress,
        arrivalDate = date,
        ourRefNumber = "11694318890095",
        weight = rawDeliveryData.totalWeight,
        fromRawDeliveryDataId = rawDeliveryData.id,
        storagePath = "not saved yet",
        customerId = rawDeliveryData.customerId,
    )

    fun handle(cmd: CreateDeliveryChannel): Unit {
        val deliveryChannel = DeliveryChannel(
            id = ObjectId().toString(),
            version = null,
            name = cmd.name,
            defaultAddress = cmd.defaultAddress,
        )
        deliveryChannelRepository.save(deliveryChannel)
    }

    fun handle(cmd: UpdateDeliveryChannel) {
        deliveryChannelRepository.findById(cmd.id).map { deliveryChannel ->
            val updatedDeliveryOrder = deliveryChannel
                .copy(defaultAddress = cmd.defaultAddress)
                .copy(name = cmd.name)
                .copy(version = cmd.expectedPreviousVersion)
            deliveryChannelRepository.save(updatedDeliveryOrder)
        }.orElseThrow { RuntimeException("Delivery channel not found: ${cmd.id}") }
    }

    fun handle(cmd: DeleteDeliveryChannel) {
        deliveryChannelRepository.deleteById(cmd.id)
    }
}
