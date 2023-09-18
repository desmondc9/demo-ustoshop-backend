package com.example.demo.deliveryorders.repositories

import com.example.demo.deliveryorders.models.RawDeliveryData
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IRawDeliveryDataRepository : PagingAndSortingRepository<RawDeliveryData, String>,
    CrudRepository<RawDeliveryData, String>, Repository<RawDeliveryData, String> {
}
