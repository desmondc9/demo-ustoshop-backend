package com.example.demo.deliveryorder.repositories

import com.example.demo.deliveryorder.models.RawDeliveryData
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IRawDeliveryDataRepository : PagingAndSortingRepository<RawDeliveryData, String>,
    CrudRepository<RawDeliveryData, String>, Repository<RawDeliveryData, String> {
}
