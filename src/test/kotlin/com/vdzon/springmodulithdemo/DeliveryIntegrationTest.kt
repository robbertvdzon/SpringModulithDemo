package com.vdzon.springmodulithdemo

import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import com.ahold.technl.delivery.web.DeliveryRequest
import com.ahold.technl.delivery.web.DeliveryResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class DeliveryIntegrationTest {

    @Autowired
    lateinit var context: WebApplicationContext

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp() {
        webTestClient = MockMvcWebTestClient.bindToApplicationContext(context).build()
    }

    @Test
    fun `moet een nieuwe levering aanmaken via REST`() {
        val request = DeliveryRequest(
            vehicleId = "VEH-999",
            address = "Dam 1, Amsterdam",
            startedAt = ZonedDateTime.now(),
            finishedAt = null,
            status = DeliveryStatus.IN_PROGRESS
        )

        val postResult = webTestClient.post()
            .uri("/deliveries")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(DeliveryResponse::class.java)
            .returnResult()

        val createdDelivery = postResult.responseBody
        // Hier kun je nu eenvoudig debuggen op 'createdDelivery'
        
        assertThat(createdDelivery).isNotNull
        assertThat(createdDelivery?.vehicleId).isEqualTo("VEH-999")
        assertThat(createdDelivery?.id).isNotNull()

        // find all deliveries and assert that the new delivery is in the list
        val getResult = webTestClient.get()
            .uri("/deliveries")
            .exchange()
            .expectBodyList(DeliveryResponse::class.java)
            .returnResult()

        val deliveries = getResult.responseBody
        // Hier kun je nu eenvoudig debuggen op 'deliveries'

        assertThat(deliveries).isNotNull
        assertThat(getResult.status).isEqualTo(HttpStatus.OK)
        val newDelivery = deliveries?.find { it.vehicleId == "VEH-999" }
        assertThat(newDelivery).isNotNull
    }
}