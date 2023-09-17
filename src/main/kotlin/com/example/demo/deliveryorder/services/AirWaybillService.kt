package com.example.demo.deliveryorder.services

import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class AirWaybillService {
    fun generateUniqueAirWaybillNumber(): String {
        return ObjectId().toString()
    }
}
