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

        mockMvc.post("/deliveries") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.vehicleId") { value("VEH-999") }
            jsonPath("$.id") { exists() }
        }
        // find all deliveries and assert that the new delivery is in the list
        val allDeliveries = mockMvc.get("/deliveries") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn().response.contentAsString
        val deliveries = objectMapper.readValue(allDeliveries, Array<DeliveryResponse>::class.java)
        val newDelivery = deliveries.find { it.vehicleId == "VEH-999" }
        assert(newDelivery != null) { "New delivery not found in list of deliveries" }
    }
}