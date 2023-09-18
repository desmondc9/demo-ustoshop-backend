package com.example.demo.deliveryorders.services

import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class AirWaybillService : IAirWaybillService {
    override fun generateUniqueAirWaybillNumber(): String {
        return ObjectId().toString()
    }
}
