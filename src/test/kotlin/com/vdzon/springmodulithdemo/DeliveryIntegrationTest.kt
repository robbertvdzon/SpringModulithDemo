package com.vdzon.springmodulithdemo

import com.vdzon.springmodulithdemo.commonmodel.DeliveryStatus
import com.ahold.technl.delivery.web.DeliveryRequest
import com.ahold.technl.delivery.web.DeliveryResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.ZonedDateTime
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.servlet.function.RequestPredicates.contentType
import tools.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import tools.jackson.core.type.TypeReference

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class DeliveryIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper


    @Test
    fun `moet een nieuwe levering aanmaken via REST`() {
        val request = DeliveryRequest(
            vehicleId = "VEH-999",
            address = "Dam 1, Amsterdam",
            startedAt = ZonedDateTime.now(),
            finishedAt = null,
            status = DeliveryStatus.IN_PROGRESS
        )

        val postResult = mockMvc.post("/deliveries") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andReturn()

        val postResponseContent = postResult.response.contentAsString
        // Hier kun je nu eenvoudig debuggen op 'postResponseContent'

        assertThat(postResult.response.status).isEqualTo(200)

        val createdDelivery = objectMapper.readValue(postResponseContent, DeliveryResponse::class.java)
        assertThat(createdDelivery.vehicleId).isEqualTo("VEH-999")
        assertThat(createdDelivery.id).isNotNull()

        // find all deliveries and assert that the new delivery is in the list
        val result = mockMvc.get("/deliveries") {
            contentType = MediaType.APPLICATION_JSON
        }.andReturn()

        val responseContent = result.response.contentAsString
        // Hier kun je nu eenvoudig debuggen op 'responseContent'

        assertThat(result.response.status).isEqualTo(200)

        val deliveries = objectMapper.readValue(responseContent, object : TypeReference<List<DeliveryResponse>>() {})
        val newDelivery = deliveries.find { it.vehicleId == "VEH-999" }
        assertThat(newDelivery).isNotNull
    }
}