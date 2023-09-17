package com.example.demo.deliveryorder.repositories

import com.example.demo.deliveryorder.models.DeliveryOrder
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IDeliveryOrderRepository : PagingAndSortingRepository<DeliveryOrder, String>,
    CrudRepository<DeliveryOrder, String>, Repository<DeliveryOrder, String> {
}
