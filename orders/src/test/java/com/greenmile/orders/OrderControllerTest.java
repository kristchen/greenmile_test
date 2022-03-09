package com.greenmile.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenmile.commons.vo.OrderVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

	@Autowired
	private MockMvc mvc;

	private OrderVo orderVo = OrderVo.builder().latitude("-3.734320").longitude("-38.530040").cargo(BigDecimal.TEN).build();

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void findById() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/28"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(28L))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findByIdNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/2888888"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findOrderWithIssues() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/with-issues"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void createOrder() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(orderVo)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findVehicle() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/28/vehicle"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findVehicleNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/33/vehicle"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findRoute() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/28/route"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findRouteNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/orders/34/route"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());
	}

}
