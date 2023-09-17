package com.example.demo.example

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@RestController
@RequestMapping("/example")
@CrossOrigin(origins = ["*"])
class ExampleController {

    @OptIn(ExperimentalEncodingApi::class)
    @PostMapping("/upload-file")
    fun uploadFile(@RequestBody request: FileUploadRequest) {
        val fileBytes = Base64.decode(request.file)
        // val fileBytes = Base64.getDecoder().decode(request.file)
        val input = fileBytes.inputStream()
        WorkbookFactory.create(input).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            sheet.forEach { row: Row ->
                row.forEach { cell: Cell ->
                    val cellValue = cell.toString()
                    print("$cellValue\t")
                }
            }
        }
    }

    @GetMapping("/download-file", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun downloadFile(): ByteArray {
        ByteArrayOutputStream().use { out ->
            PDDocument().use {
                it.documentInformation.also { it.title = "Hello World" }

                val page1 = PDPage(PDRectangle.A4)
                it.addPage(page1)

                PDPageContentStream(it, page1).use { content ->
                    content.beginText()
                    content.setFont(PDType1Font(FontName.TIMES_ROMAN), 12f)
                    content.newLineAtOffset(25.0f, page1.bBox.height - 25)

                    content.showText("Hello World 1")
                    content.newLineAtOffset(0f, -20f)
                    content.showText("Hello World 2")
                    content.newLineAtOffset(0f, -20f)
                    content.showText("Hello World 3")

                    content.endText()

                    drawTable(
                        page1,
                        content,
                        700f,
                        20f,
                        listOf(
                            listOf("Header 1", "Header 2", "Header 3"),
                            listOf("Row 1, Cell 1", "Row 1, Cell 2", "Row 1, Cell 3"),
                            listOf("Row 2, Cell 1", "Row 2, Cell 2", "Row 2, Cell 3"),
                        )
                    )
                }
                it.save(out)
            }
            return out.toByteArray()
        }
    }

    fun drawTable(
        page: PDPage,
        contentStream: PDPageContentStream,
        y: Float,
        margin: Float,
        content: List<List<String>>,
    ) {
        val rows = content.size
        val cols = content[0].size
        val tableWidth = page.getMediaBox().width - 2 * margin
        val tableHeight = 50.0f
        val colWidth = tableWidth / cols.toFloat()
        val rowHeight = tableHeight / rows.toFloat()
        val tableTopY = y

        // Draw the rows
        var nextY = y
        for (i in 0..rows) {
            contentStream.moveTo(margin, nextY)
            contentStream.lineTo(margin + tableWidth, nextY)
            contentStream.stroke()
            nextY -= rowHeight
        }

        // Draw the columns
        var nextX = margin
        for (i in 0..cols) {
            contentStream.moveTo(nextX, y)
            contentStream.lineTo(nextX, y - tableHeight)
            contentStream.stroke()
            nextX += colWidth
        }

        // Now add the text
        contentStream.setFont(PDType1Font(FontName.TIMES_ROMAN), 12.0f)
        var textx = margin + colWidth / 2 - 20
        var texty = y - 15
        for (i in content.indices) {
            for (j in content[i].indices) {
                val text = content[i][j]
                contentStream.beginText()
                contentStream.newLineAtOffset(textx, texty)
                contentStream.showText(text)
                contentStream.endText()
                textx += colWidth
            }
            texty -= rowHeight
            textx = margin + colWidth / 2 - 20
        }
    }
}

data class FileUploadRequest(val file: String)
