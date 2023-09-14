package com.example.demo.example

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest(
    @Autowired val client: TestRestTemplate,
) {

    @Test
    fun `it should download file`() {
        val actual = client.getForEntity("/example/download-file", ByteArray::class.java)

        assertThat(actual.body).isNotEmpty()
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `it should upload file`() {
        // Given
        val fileResource = ClassPathResource("example_egxcel.xlsx")
        val fileBytes = fileResource.contentAsByteArray
        val fileContentBase64 = Base64.encode(fileBytes)
        val request = FileUploadRequest(file = fileContentBase64)

        // When
        val actual = client.postForEntity<String>("/example/upload-file", request, Unit::class.java)

        // Then
        assertThat(actual.statusCode).isEqualTo(HttpStatus.OK)
    }


}
