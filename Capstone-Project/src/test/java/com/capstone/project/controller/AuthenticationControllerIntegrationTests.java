package com.capstone.project.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.capstone.project.CapstoneProjectApplication;
import com.capstone.project.bean.Employee;
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
	
	@LocalServerPort
	private int port;
	
	String token = null;
	
	@BeforeEach
	public void initEach() {
		token = "Bearer "+authenticationService.generateJWTToken("admin", "admin").getJwt();
	}
	
	@Test
	@DisplayName("MockMVCIntegrationTest-Unauthorized User")
    public void unAuthorized_User_Ok() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(createURLWithPort("/getEmployee"))).andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("MockMVCIntegrationTest-Authorized User")
    public void authorized_User_Ok() throws Exception {
    	System.out.println("*********** "+token);
    	
    	List<Employee> empList = new ArrayList<>();
		Employee employee1 = new Employee(1,"Test-1", "Test-1@it.com", "IT");
		Employee employee2 = new Employee(2,"Test-2", "Test-2@it.com", "IT");
		empList.add(employee1);
		empList.add(employee2);

		when(employeeRepository.findAll()).thenReturn(empList);

//		String expected = om.writeValueAsString(empList);
//
//		ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/getEmployee"), String.class);
    	
    	assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get(createURLWithPort("/getEmployee")).header("Authorization", token)).andExpect(status().isOk());
    }
    
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + "/capstone/api/v1" + uri;
	}

}
