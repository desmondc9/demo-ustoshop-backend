package com.example.demo.deliveryorder.services

import com.example.demo.deliveryorder.services.DeliveryOrderTestHelper.Companion.generateDeliveryOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

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
}
