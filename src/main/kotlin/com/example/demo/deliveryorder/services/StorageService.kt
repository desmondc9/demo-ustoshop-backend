package com.example.demo.deliveryorder.services

import com.example.demo.deliveryorder.models.DeliveryOrder
import org.springframework.stereotype.Service
import java.nio.file.Files
import kotlin.io.path.Path

@Service
class StorageService {

    fun save(content: ByteArray, deliveryOrder: DeliveryOrder) {
        val generatedPath = generatePathFor(deliveryOrder)
        Files.write(Path(generatedPath), content)
    }

    fun generatePathFor(deliveryOrder: DeliveryOrder): String {
        return "/tmp/${deliveryOrder.airWaybillNumber}-${deliveryOrder.id}.pdf"
    }

}
