package com.example.demo.deliveryorder.services

import com.example.demo.deliveryorder.models.DeliveryOrder
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Service
class PdfService {
    fun generatePdf(deliveryOrder: DeliveryOrder): ByteArray {
        ByteArrayOutputStream().use { out ->
            PDDocument().use { pdfDocument ->
                val page1 = PDPage(PDRectangle.LETTER)
                pdfDocument.addPage(page1)
                PDPageContentStream(pdfDocument, page1).use { content ->
                    content.beginText()
                    content.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20f)

                    content.newLineAtOffset(25.0f, page1.bBox.height - 60)
                    content.showText("DELIVERY ORDER")

                    content.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)

                    content.newLineAtOffset(0f, -20f)
                    // content.showText(deliveryOrder.issuer)
                    deliveryOrder.issuer.split("\n").forEach {
                        content.showText(it)
                        content.newLineAtOffset(0f, -15f)
                    }

                    content.newLineAtOffset(300f, 0f)
                    content.showText(deliveryOrder.issuedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
                    content.newLineAtOffset(100f, 0f)
                    content.showText("OUR REF. NO")
                    content.newLineAtOffset(0f, -10f)
                    content.showText(deliveryOrder.ourRefNumber)

                    content.newLineAtOffset(-400f, -30f)
                    content.showText("CARRIER:")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("LOCATION:")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("ORIGINAL/DESTINATION PORT:")

                    content.newLineAtOffset(-320f, -15f)
                    content.showText(deliveryOrder.carrier)
                    content.newLineAtOffset(160f, 0f)
                    content.showText(deliveryOrder.location)
                    content.newLineAtOffset(160f, 0f)
                    content.showText("${deliveryOrder.originalPort}/${deliveryOrder.destinationPort}")

                    content.newLineAtOffset(-320f, -30f)
                    content.showText("B/L OR AWB NO")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("ARRIVAL DATE")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("FREE TIME EXP")

                    content.newLineAtOffset(-320f, -15f)
                    content.showText(deliveryOrder.airWaybillNumber)
                    content.newLineAtOffset(160f, 0f)
                    content.showText(deliveryOrder.arrivalDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
                    content.newLineAtOffset(160f, 0f)
                    content.showText(
                        deliveryOrder.freeTimeExpired
                            ?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: ""
                    )

                    content.newLineAtOffset(-320f, -30f)
                    content.showText("HOUSE NO.")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("ENTRY-B/L NO.")
                    content.newLineAtOffset(160f, 0f)
                    content.showText("CUST. REF. NO.")

                    content.newLineAtOffset(-320f, -15f)
                    content.showText(deliveryOrder.houseNumber)
                    content.newLineAtOffset(160f, 0f)
                    content.showText(deliveryOrder.entryBillOfLadingNumber)
                    content.newLineAtOffset(160f, 0f)
                    content.showText(deliveryOrder.customsRefNumber)

                    content.newLineAtOffset(-320f, -30f)
                    content.showText("FOR DELIVERY TO")
                    content.newLineAtOffset(420f, 0f)
                    content.showText("ROUTE")

                    content.newLineAtOffset(-420f, 0f)
                    deliveryOrder.addressForDeliveryTo.split("\n").forEach {
                        content.newLineAtOffset(0f, -15f)
                        content.showText(it)
                    }

                    val addressLineCount = deliveryOrder.addressForDeliveryTo.split("\n").size
                    content.newLineAtOffset(420f, if (addressLineCount == 1) 0f else 9f * addressLineCount)
                    content.showText(deliveryOrder.route)

                    content.newLineAtOffset(-420f, -50f)
                    content.showText("No. of PKGS")
                    content.newLineAtOffset(100f, 0f)
                    content.showText("DESCRIPTION OF ARTICLES, SPECIAL MARKS & EXCEPTIONS")
                    content.newLineAtOffset(400f, 0f)
                    content.showText("WEIGHT")

                    content.newLineAtOffset(-500f, -80f)
                    content.showText("${deliveryOrder.numberOfPackages} pcs")
                    content.newLineAtOffset(100f, 0f)
                    content.showText(deliveryOrder.descriptions)
                    content.newLineAtOffset(400f, 0f)
                    content.showText("${deliveryOrder.weight.value} ${deliveryOrder.weight.unit.value}")

                    // Changed to column stack
                    content.newLineAtOffset(-500f, -80f)
                    content.showText("TYPE OF SERVICE")
                    content.newLineAtOffset(0f, -15f)
                    content.showText(deliveryOrder.type.value)

                    content.newLineAtOffset(180f, 15f)
                    content.showText("PAYMENT TYPE")
                    content.newLineAtOffset(0f, -15f)
                    content.showText(deliveryOrder.paymentType.value)

                    content.newLineAtOffset(180f, 15f)
                    content.showText("Received in Good Order")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("By")
                    content.newLineAtOffset(0f, -10f)
                    content.showText("_________________________")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("Date:")
                    content.newLineAtOffset(70f, 0f)
                    content.showText("Time:")

                    content.newLineAtOffset(-430f, -100f)
                    content.showText("Location of Goods:")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("3245 Story Roa West")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("IRVING, TX 75038")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("CHECK AVAILABILITY PHONE#: 569-565-2088")


                    content.newLineAtOffset(380f, 0f)
                    content.showText("DELIVERY CLERK: DELIVER")
                    content.newLineAtOffset(0f, -15f)
                    content.showText("TO CARRIER SHOWN ABOVE")
                    // End of column stack

                    content.endText()
                }
                pdfDocument.save(out)
            }
            return out.toByteArray()
        }
    }
}
