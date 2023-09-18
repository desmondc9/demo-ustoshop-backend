package com.example.demo.deliveryorders.repositories

import com.example.demo.deliveryorders.models.DeliveryChannel
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IDeliveryChannelRepository : PagingAndSortingRepository<DeliveryChannel, String>,
    CrudRepository<DeliveryChannel, String>, Repository<DeliveryChannel, String> {
}
