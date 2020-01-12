package com.capstone.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.capstone.project.CapstoneProjectApplication;
import com.capstone.project.config.Constants;
import com.capstone.project.repository.EmployeeRepository;
import com.capstone.project.service.AuthenticationService;

@SpringBootTest(classes = CapstoneProjectApplication.class, 
webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
@DisplayName("MockMVC Integration Test")
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTests {

	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private EmployeeRepository employeeRepository;
	
	//private WebApplicationContext context;
	
	@LocalServerPort
	private int port;
	
	String token = null;
	
	@BeforeEach
	public void initEach() {
		token = "Bearer "+authenticationService.generateJWTToken("admin", "test1234").getJwt();
	}
	
	@Test
	@DisplayName("MockMVCIntegrationTest-Unauthorized User")
    public void unAuthorized_User_Ok() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get(createURLWithPort("/getEmployee"))).andExpect(status().isForbidden());
		mvc.perform(MockMvcRequestBuilders.get(createURLWithPort("/getEmployee"))).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
	
    
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + "/capstone/api/v1" + uri;
	}

}
