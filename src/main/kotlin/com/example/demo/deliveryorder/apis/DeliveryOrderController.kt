package com.example.demo.deliveryorder.apis

import com.example.demo.deliveryorder.CreateDeliveryChannel
import com.example.demo.deliveryorder.DeleteDeliveryChannel
import com.example.demo.deliveryorder.DeliveryOrderQueryHandler
import com.example.demo.deliveryorder.services.DeliveryOrderCommandHandler
import com.example.demo.deliveryorder.ImportRawDeliveryData
import com.example.demo.deliveryorder.UpdateDeliveryChannel
import com.example.demo.deliveryorder.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorder.models.valueobjects.Weight
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/delivery-orders")
@Validated
@CrossOrigin(origins = ["*"])
class DeliveryOrderController(
    val deliveryOrderCommandHandler: DeliveryOrderCommandHandler,
    val deliveryOrderQueryHandler: DeliveryOrderQueryHandler,
) {
    @PostMapping("/input-user-input-delivery-order-data")
    fun importRawDeliveryData(
        @Valid @RequestBody request: RawDeliveryDataRequest,
        authentication: Authentication,
    ): Unit = try {
        val cmd = ImportRawDeliveryData(
            billOfLadingNumber = request.billOfLadingNumber,
            customerId = authentication.name,
            totalWeight = request.totalWeight,
            totalNumberOfBoxes = request.totalNumberOfBoxes,
            channelCompositions = request.channelCompositions,
        )
        deliveryOrderCommandHandler.handle(cmd)
    } catch (e: Exception) {
        when (e) {
            is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/delivery-channels")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    fun createDeliveryChannel(
        @Valid @RequestBody request: CreateDeliveryChannelRequest,
    ) {
        try {
            val cmd = CreateDeliveryChannel(
                name = request.name,
                defaultAddress = request.defaultAddress,
            )
            deliveryOrderCommandHandler.handle(cmd)
        } catch (e: Exception) {
            when (e) {
                is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
                else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            }
        }
    }


    @GetMapping("/delivery-channels")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun queryAllDeliveryChannels(
    ): AllDeliveryChannelsView = try {
        AllDeliveryChannelsView(
            data = deliveryOrderQueryHandler.queryAllDeliveryChannels().map {
                AllDeliveryChannelsViewLine(
                    id = it.id,
                    version = it.version ?: throw RuntimeException("version is null"),
                    name = it.name,
                    defaultAddress = it.defaultAddress
                )
            }
        )
    } catch (e: Exception) {
        when (e) {
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }


    @PutMapping("/delivery-channels")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateDeliveryChannel(
        @Valid @RequestBody request: UpdateDeliveryChannelRequest,
    ) {
        try {
            val cmd = UpdateDeliveryChannel(
                id = request.id,
                expectedPreviousVersion = request.expectedPreviousVersion,
                name = request.name,
                defaultAddress = request.defaultAddress,
            )
            deliveryOrderCommandHandler.handle(cmd)
        } catch (e: Exception) {
            when (e) {
                is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
                is OptimisticLockingFailureException -> throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
                else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            }
        }
    }

    @DeleteMapping("/delivery-channels/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteDeliveryChannel(
        @PathVariable("id") id: String,
    ) {
        try {
            val cmd = DeleteDeliveryChannel(id = id)
            deliveryOrderCommandHandler.handle(cmd)
        } catch (e: Exception) {
            when (e) {
                is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
                else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            }
        }
    }
}


data class RawDeliveryDataRequest(
    @NotNull val billOfLadingNumber: String,
    @NotNull val customerId: String,
    @Positive val totalWeight: Weight,
    @NotNull val totalNumberOfBoxes: Int,
    @NotEmpty val channelCompositions: List<ChannelComposition>,
)

data class CreateDeliveryChannelRequest(
    @NotNull val name: String,
    @NotNull val defaultAddress: String,
)

data class UpdateDeliveryChannelRequest(
    @NotNull val id: String,
    @NotNull val expectedPreviousVersion: Long,
    @NotNull val name: String,
    @NotNull val defaultAddress: String,
)


data class AllDeliveryChannelsView(
    val data: List<AllDeliveryChannelsViewLine>,
)

data class AllDeliveryChannelsViewLine(
    val id: String,
    val version: Long,
    val name: String,
    val defaultAddress: String,
)
