package com.greenmile.routes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenmile.commons.vo.RouteVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RoutesControllerTest {


	private RouteVo routeVo = RouteVo.builder().vehicleId(3L).build();

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MockMvc mvc;

	@Test
	void createRoute() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/routes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(routeVo)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getRoutes() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/routes"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findById() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/routes/11"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(11L))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void findByIdNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/routes/2888888"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getStatistcs() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/routes/statistics"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(("application/json")))
				.andDo(MockMvcResultHandlers.print());
	}

}
