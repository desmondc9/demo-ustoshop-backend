package com.example.demo.deliveryorders.services

import com.example.demo.MongoDBTest
import com.example.demo.auth.models.Authority
import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.core.nextString
import com.example.demo.deliveryorders.models.valueobjects.ChannelComposition
import com.example.demo.deliveryorders.repositories.IDeliveryChannelRepository
import com.example.demo.deliveryorders.repositories.IDeliveryOrderRepository
import com.example.demo.deliveryorders.services.DeliveryOrderTestHelper.Companion.generateRawDeliveryData
import com.example.demo.users.UserTestHelper.Companion.adminPassword
import com.example.demo.users.UserTestHelper.Companion.adminUsername
import com.example.demo.users.UserTestHelper.Companion.createUserInDatabase
import com.example.demo.users.repositories.IUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.random.Random


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryOrderControllerTest(
    @Autowired val client: TestRestTemplate,
    @Autowired val deliveryOrderRepository: IDeliveryOrderRepository,
    @Autowired val deliveryChannelRepository: IDeliveryChannelRepository,
    @Autowired val userReposiotry: IUserRepository,
    @Autowired val userAuthoritiesRepository: IUserAuthoritiesRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
) : MongoDBTest() {
    @BeforeEach
    fun setUp() {
        deliveryOrderRepository.deleteAll()
        deliveryChannelRepository.deleteAll()
        userReposiotry.deleteAll()
        userAuthoritiesRepository.deleteAll()
    }

    @Nested
    inner class `Given delivery orders` {
        @Test
        fun `it should let user import RawDeliveryData`() {
            val rawDeliveryData = generateRawDeliveryData()
            val request = ImportRawDeliveryData(
                billOfLadingNumber = rawDeliveryData.billOfLadingNumber,
                customerId = rawDeliveryData.customerId,
                totalWeight = rawDeliveryData.totalWeight,
                totalNumberOfBoxes = rawDeliveryData.totalNumberOfBoxes,
                channelCompositions = rawDeliveryData.channelCompositions,
            )
        }
    }

    @Nested
    inner class `Given a delivery channel` {

        @Test
        fun `it should create a DeliveryChannel with ADMIN authority`() {
            createUserInDatabase(
                username = adminUsername,
                password = adminPassword,
                userReposiotry::save,
                userAuthoritiesRepository::save,
                listOf(Authority.ADMIN)
            )

            val request = CreateDeliveryChannel(name = "UPS", defaultAddress = Random.nextString(20))
            val headers = HttpHeaders().also { it.setBasicAuth(adminUsername, adminPassword) }
            val httpEntity = HttpEntity(request, headers)

            val response =
                client.exchange("/delivery-orders/delivery-channels", HttpMethod.POST, httpEntity, Unit::class.java)

            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)

            val actual = deliveryChannelRepository.findAll().first()
            assertThat(actual.id).isNotEmpty()
            assertThat(actual.name).isEqualTo(request.name)
            assertThat(actual.defaultAddress).isEqualTo(request.defaultAddress)
            assertThat(actual.version).isEqualTo(0)
        }

        @Test
        fun `it should not create a DeliveryChannel without ADMIN authority`() {
            createUserInDatabase(
                username = "nonAdmin",
                password = "password",
                userReposiotry::save,
                userAuthoritiesRepository::save
            )

            val request = CreateDeliveryChannel(name = "UPS", defaultAddress = Random.nextString(20))
            val headers = HttpHeaders().also { it.setBasicAuth("nonAdmin", "password") }
            val httpEntity = HttpEntity(request, headers)

            val response =
                client.exchange("/delivery-orders/delivery-channels", HttpMethod.POST, httpEntity, Unit::class.java)

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }
    }

}
