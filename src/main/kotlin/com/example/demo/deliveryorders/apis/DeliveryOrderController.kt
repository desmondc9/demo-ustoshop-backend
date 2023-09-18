package com.example.demo.deliveryorders.apis

import com.example.demo.auth.models.SecurityUser
import com.example.demo.deliveryorders.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorders.models.valueobjects.Weight
import com.example.demo.deliveryorders.services.CreateDeliveryChannel
import com.example.demo.deliveryorders.services.DeleteDeliveryChannel
import com.example.demo.deliveryorders.services.DeliveryOrderCommandHandler
import com.example.demo.deliveryorders.services.DeliveryOrderList
import com.example.demo.deliveryorders.services.DeliveryOrderQueryHandler
import com.example.demo.deliveryorders.services.ImportRawDeliveryData
import com.example.demo.deliveryorders.services.UpdateDeliveryChannel
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
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
class DeliveryOrderController(
    val deliveryOrderCommandHandler: DeliveryOrderCommandHandler,
    val deliveryOrderQueryHandler: DeliveryOrderQueryHandler,
) {
    @PostMapping("/import-raw-delivery-data")
    fun importRawDeliveryData(
        @Valid @RequestBody request: RawDeliveryDataRequest,
        @AuthenticationPrincipal userDetail: SecurityUser,
    ): Unit = try {
        val cmd = ImportRawDeliveryData(
            billOfLadingNumber = request.billOfLadingNumber,
            customerId = userDetail.getUserId(),
            totalWeight = request.totalWeight,
            totalNumberOfBoxes = request.totalNumberOfBoxes,
            channelCompositions = request.channelCompositions,
        )
        val result = deliveryOrderCommandHandler.handle(cmd)
        result
    } catch (e: Exception) {
        when (e) {
            is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping()
    fun queryDeliveryOrders(
        @AuthenticationPrincipal userDetail: SecurityUser,
    ): DeliveryOrderList = try {
        val result = deliveryOrderQueryHandler.queryDeliveryOrdersByCustomerId(userDetail.getUserId())
        result
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
    }


    // there are some securities issues, in production, need to change to a storage service like AWS S3, and enable access control
    @GetMapping("/pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun downloadPdf(
        @RequestParam path: String,
    ): ByteArray = try {
        val result = deliveryOrderQueryHandler.downloadPdf(path)
        result
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
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
            val result = deliveryOrderCommandHandler.handle(cmd)
            result
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
    ) = try {
        val result = deliveryOrderQueryHandler.queryAllDeliveryChannels()
        result
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
    }


    @PutMapping("/delivery-channels")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateDeliveryChannel(
        @Valid @RequestBody request: UpdateDeliveryChannelRequest,
    ) = try {
        val cmd = UpdateDeliveryChannel(
            id = request.id,
            expectedPreviousVersion = request.expectedPreviousVersion,
            name = request.name,
            defaultAddress = request.defaultAddress,
        )
        val result = deliveryOrderCommandHandler.handle(cmd)
        result
    } catch (e: Exception) {
        when (e) {
            is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
            is OptimisticLockingFailureException -> throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }


    @DeleteMapping("/delivery-channels/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteDeliveryChannel(
        @PathVariable("id") id: String,
    ) =
        try {
            val cmd = DeleteDeliveryChannel(id = id)
            val result = deliveryOrderCommandHandler.handle(cmd)
            result
        } catch (e: Exception) {
            when (e) {
                is MethodArgumentNotValidException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
                else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            }
        }
}

data class RawDeliveryDataRequest(
    @NotNull val billOfLadingNumber: String,
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
