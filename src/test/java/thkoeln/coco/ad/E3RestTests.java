package thkoeln.coco.ad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import thkoeln.coco.ad.miningMachine.MiningMachine;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class E3RestTests {

	private MockMvc mockMvc;
	private final String marvinJson = "{ \"name\": \"Marvin\" }";
	private final String robbiJson = "{ \"name\": \"Robbi\" }";
	private final String fliwatuetJson = "{ \"name\": \"Fliwatuet\" }";
	private ObjectMapper objectMapper = new ObjectMapper();


	@Autowired
	public E3RestTests( MockMvc mockMvc  ) {
		this.mockMvc = mockMvc;
	}


	/**
	 * Test a simple creation
	 */
	@Test
	public void testMiningMachineCreation() throws Exception {
		MvcResult result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( get("/miningMachines/" + id )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isOk())
				.andReturn();
	}

	/**
	 * Test a the conflict, if 2nd mining machine is created
	 */
	@Test
	public void testTwoMiningMachineCreations() throws Exception {
		mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(robbiJson))
				.andExpect(status().isConflict())
				.andReturn();
	}

	/**
	 * Create 1st mining machine, move it, then create 2nd
	 */
	@Test
	public void testTwoMiningMachineCreationsWithMove() throws Exception {
		MvcResult result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[no,1]" ) )
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(robbiJson))
				.andExpect(status().isCreated())
				.andReturn();
	}


	/**
	 * Create 1st mining machine, move it, then create 2nd, try to move it to the same spot (should be unsuccessful),
	 * then create 3rd (must cause conflict)
	 */
	@Test
	public void testThreeMiningMachineCreationsWithConflict() throws Exception {
		MvcResult result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[no,1]" ) )
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(robbiJson))
				.andExpect(status().isCreated())
				.andReturn();
		id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[no,1]" ) )
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(robbiJson))
				.andExpect(status().isConflict())
				.andReturn();
	}

	/**
	 * Create 1st mining machine, move it, then create 2nd, move it too, then 3rd
	 */
	@Test
	public void testThreeMiningMachineCreationsWithSuccessfulMoves() throws Exception {
		MvcResult result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[no,1]" ) )
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(robbiJson))
				.andExpect(status().isCreated())
				.andReturn();
		id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[ea,1]" ) )
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(fliwatuetJson))
				.andExpect(status().isCreated())
				.andReturn();
	}


	/**
	 * Fetch a mining machine that doesn't exist
	 */
	@Test
	public void testGetNonExistingMiningMachine() throws Exception {
		mockMvc.perform( get("/miningMachines/7dbbe77b-aaed-40ab-8799-13e68c49cb28" ) )
				.andExpect(status().isNotFound())
				.andReturn();
	}

	/**
	 * Create 1st mining machine, and supply an invalid task
	 */
	@Test
	public void testInvalidTask() throws Exception {
		MvcResult result = mockMvc.perform( post("/miningMachines" )
				.contentType(APPLICATION_JSON).content(marvinJson))
				.andExpect(status().isCreated())
				.andReturn();
		String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		mockMvc.perform( get("/miningMachines/" + id ) )
				.andExpect(status().isOk())
				.andReturn();
		mockMvc.perform( put("/miningMachines/" + id + "/tasks/[xx,1]" ) )
				.andExpect(status().is4xxClientError())
				.andReturn();
	}
}
