package com.capstone.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.capstone.project.CapstoneProjectApplication;
import com.capstone.project.bean.Employee;
import com.capstone.project.config.Constants;
import com.capstone.project.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CapstoneProjectApplication.class, 
webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
@DisplayName("Integration Test")
class EmployeeControllerIntegrationTests {

	private static final ObjectMapper om = new ObjectMapper();

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private EmployeeRepository employeeRepository;

	@BeforeEach
	public void initEach() {
		Employee employee = new Employee(1, "Test-1", "Test-1@it.com", "IT");
		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
	}

	@Test
	@DisplayName("IntegrationTest-Save Employee")
	public void save_Employee_Ok() throws Exception {
		Employee employee = new Employee(1, "Test-1", "Test-1@it.com", "IT");
		when(employeeRepository.save(employee)).thenReturn(employee);
		//String expected = om.writeValueAsString(employee);
		ResponseEntity<String> responseEntity = this.restTemplate
				.postForEntity(createURLWithPort("/addEmployee"), employee, String.class);
		assertEquals(201, responseEntity.getStatusCodeValue());
		//JSONAssert.assertEquals(expected, responseEntity.getBody(), false);
		verify(employeeRepository, times(1)).save(any(Employee.class));

	}

	@Test
	@DisplayName("IntegrationTest-Find Employee by Id")
	public void find_EmployeeId_Ok() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity(
				createURLWithPort("/getEmployee/1"), String.class);
		String expected = "{\"empId\":1,\"empName\":\"Test-1\",\"emailId\":\"Test-1@it.com\",\"departmentName\":\"IT\"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(employeeRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("IntegrationTest-Find all Employee")
	public void find_AllEmployee_Ok() throws Exception {

		List<Employee> empList = new ArrayList<>();
		Employee employee1 = new Employee(1,"Test-1", "Test-1@it.com", "IT");
		Employee employee2 = new Employee(2,"Test-2", "Test-2@it.com", "IT");
		empList.add(employee1);
		empList.add(employee2);

		when(employeeRepository.findAll()).thenReturn(empList);

		String expected = om.writeValueAsString(empList);

		ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/getEmployee"), String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("IntegrationTest-Employee Not Found - 404")
	public void find_employeeIdNotFound_404() throws Exception {
		String expected = "{\"timestamp\":null,\"message\":\"Employee not found for this id :: 5\",\"details\":\"uri=/capstone/api/getEmployee/5\"}";
		ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/getEmployee/5"), String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@DisplayName("IntegrationTest-Update Employee")
	public void update_Employee_Ok() throws Exception {
		Employee updatedEmployee = new Employee(1,"Test-1", "Test-1@it.com", "IT");
		when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updatedEmployee), headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/updateEmployee"), HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(om.writeValueAsString(updatedEmployee), response.getBody(), false);

		verify(employeeRepository, times(1)).findById(1);
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}

	@Test
	@DisplayName("IntegrationTest-Delete Employee")
	public void delete_Employee_Ok() {
		doNothing().when(employeeRepository).delete(any(Employee.class));

		HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/deleteEmployee/1"), HttpMethod.DELETE, entity, String.class);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(employeeRepository, times(1)).delete(any(Employee.class));
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + "/capstone/api" + uri;
	}

}
