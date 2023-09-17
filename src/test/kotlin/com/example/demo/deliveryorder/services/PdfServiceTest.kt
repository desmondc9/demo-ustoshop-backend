package com.example.demo.deliveryorder.services

import com.example.demo.core.nextString
import com.example.demo.deliveryorder.models.DeliveryOrder
import com.example.demo.deliveryorder.models.valueobjects.Weight
import com.example.demo.deliveryorder.models.valueobjects.WeightUnit
import com.example.demo.deliveryorder.services.DeliveryOrderTestHelper.Companion.generateDeliveryOrder
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.random.Random

class PdfServiceTest(
) {
    private val pdfService: PdfService = PdfService()

    @Test
    fun `it should generate pdf`() {
        // Given
        val deliveryOrder = generateDeliveryOrder()

        // When
        val pdf = pdfService.generatePdf(deliveryOrder)
        val pdfFile =
            Files.write(
                Path("src/test/resources/delivery-order.pdf"),
                pdf,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )

        // Then
        assertThat(pdfFile).exists()
    }

    @Test
    fun `it should generate pdf when issuer address is multiple lines`() {
        // Given
        // val deliveryOrder = generateDeliveryOrder()
        val ustoshopAddress = """USTOSHOP.COM INC
                                     |3245 Story Road West
                                     |Irving, TX 75038""".trimMargin()
        val deliveryOrder = DeliveryOrder(
            id = ObjectId().toString(),
            version = 0,
            issuer = ustoshopAddress,
            issuedDate = LocalDate.now(),
            airWaybillNumber = Random.nextString(),
            numberOfPackages = Random.nextInt(0, 9999),
            addressForDeliveryTo = ustoshopAddress,
            arrivalDate = LocalDate.now(),
            ourRefNumber = Random.nextString(),
            weight = Weight(value = Random.nextInt(0, 1000), unit = WeightUnit.KG),
            fromRawDeliveryDataId = ObjectId().toString(),
            carrier = Random.nextString(),
            location = Random.nextString(),
            originalPort = Random.nextString(3),
            destinationPort = Random.nextString(3),
            freeTimeExpired = LocalDate.now().plusDays(Random.nextLong(1, 10)),
            houseNumber = Random.nextString(),
            entryBillOfLadingNumber = Random.nextString(),
            customsRefNumber = Random.nextString(),
            route = Random.nextString(),

            )
        // When
        val pdf = pdfService.generatePdf(deliveryOrder)
        val pdfFile =
            Files.write(
                Path("src/test/resources/delivery-order.pdf"),
                pdf,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )

        // Then
        assertThat(pdfFile).exists()
    }
}
