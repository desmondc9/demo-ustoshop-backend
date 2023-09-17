package com.example.demo.deliveryorder.services

import com.example.demo.core.nextString
import com.example.demo.deliveryorder.models.DeliveryOrder
import com.example.demo.deliveryorder.models.valueobjects.Weight
import com.example.demo.deliveryorder.models.valueobjects.WeightUnit
import org.bson.types.ObjectId
import java.time.LocalDate
import kotlin.random.Random

class DeliveryOrderTestHelper {
    companion object {
        fun generateDeliveryOrder(): DeliveryOrder {
            return DeliveryOrder(
                id = ObjectId().toString(),
                version = Random.nextLong(),
                issuer = Random.nextString(),
                issuedDate = LocalDate.now(),
                airWaybillNumber = Random.nextString(),
                numberOfPackages = Random.nextInt(0, 9999),
                addressForDeliveryTo = """${Random.nextString()}
                    |${Random.nextString()}
                    |${Random.nextString()}""".trimMargin(),
                arrivalDate = LocalDate.now(),
                ourRefNumber = Random.nextString(),
                weight = Weight(value = Random.nextInt(0, 1000), unit = WeightUnit.KG),
                fromRawDeliveryDataId = ObjectId().toString(),
                carrier = Random.nextString(),
                location = Random.nextString(),
                originalPort = Random.nextString(3),
                destinationPort = Random.nextString(3),
                freeTimeExpired = LocalDate.now().plusDays(Random.nextLong(1, 10)),
                houseNumber = Random.nextString(),
                entryBillOfLadingNumber = Random.nextString(),
                customsRefNumber = Random.nextString(),
                route = Random.nextString(),
            )
        }
    }
}
