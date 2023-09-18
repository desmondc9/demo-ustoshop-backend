package com.example.demo.deliveryorders.services

import com.example.demo.deliveryorders.models.DeliveryOrder
import org.springframework.stereotype.Service
import java.nio.file.Files
import kotlin.io.path.Path

@Service
class StorageService : IStorageService {

    override fun save(content: ByteArray, deliveryOrder: DeliveryOrder) {
        val generatedPath = generatePathFor(deliveryOrder)
        Files.write(Path(generatedPath), content)
    }

    override fun generatePathFor(deliveryOrder: DeliveryOrder): String {
        return "/tmp/${deliveryOrder.airWaybillNumber}-${deliveryOrder.id}.pdf"
    }
}
