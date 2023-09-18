package com.example.demo.deliveryorders.repositories

import com.example.demo.deliveryorders.models.DeliveryOrder
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IDeliveryOrderRepository : PagingAndSortingRepository<DeliveryOrder, String>,
    CrudRepository<DeliveryOrder, String>, Repository<DeliveryOrder, String> {
}
