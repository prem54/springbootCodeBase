package com.capstone.project.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.capstone.project.bean.Employee;
import com.capstone.project.config.Constants;
import com.capstone.project.exceptions.ResourceNotFoundException;
import com.capstone.project.repository.EmployeeRepository;

@ActiveProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
@SpringBootTest
@DisplayName("Test Controller")
class EmployeeControllerTest {

	@Autowired
	EmployeeController employeeController;

	@MockBean
	EmployeeRepository employeeRepository;

	@BeforeEach
	public void initEach() {
		Employee employee = new Employee(1, "Test-1", "Test-1@it.com", "IT");
		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
	}

	@Test
	@DisplayName("ControllerTest-Add Employee")
	public void add_Employee_Ok() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		Employee employee = new Employee(1, "Test-101", "Test-101@it.com", "IT");

		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);


		ResponseEntity<Employee> responseEntity = employeeController.addEmployee(employee);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
		assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");
	}

	@Test
	@DisplayName("ControllerTest-Find Employee by Id")
	public void find_EmployeeById_Ok() throws ResourceNotFoundException {

		//		MockHttpServletRequest request = new MockHttpServletRequest();
		//		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		Optional<Employee> employee = Optional.of(new Employee(1,"Test-101", "Test-101@it.com", "IT"));

		when(employeeRepository.findById(1)).thenReturn(employee);

		ResponseEntity<Employee> responseEntity = employeeController.getEmployeeById(1);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals(responseEntity.getBody().getEmpId(), 1);
	}

	@Test
	@DisplayName("ControllerTest-Get all Employee")
	public void find_AllEmployee_Ok() {

		List<Employee> empList = new ArrayList<>();
		Employee employee1 = new Employee(1,"Test-101", "Test-101@it.com", "IT");
		Employee employee2 = new Employee(2,"Test-102", "Test-102@it.com", "IT");
		empList.add(employee1);
		empList.add(employee2);

		when(employeeRepository.findAll()).thenReturn(empList);

		List<Employee> employees = employeeController.getAllEmployee();

		assertThat(employees.size()).isEqualTo(2);
		assertThat(employees.get(0).getEmpName()).isEqualTo(employee1.getEmpName());
	}

	@Test
	@DisplayName("ControllerTest-Delete Employee")
	public void delete_Employee_Ok() throws ResourceNotFoundException {

		doNothing().when(employeeRepository).delete(any(Employee.class));

		ResponseEntity<Void> response = employeeController.deleteEmployee(1);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(employeeRepository, times(1)).delete(any(Employee.class));
	}

	@Test
	@DisplayName("ControllerTest-Update Employee")
	public void update_Employee_Ok() throws Exception {
		Employee updatedEmployee = new Employee(1, "Test-101", "Test-101@it.com", "IT");
		when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

		ResponseEntity<Employee> response = employeeController.updateEmployee(updatedEmployee);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedEmployee, response.getBody());

		verify(employeeRepository, times(1)).findById(1);
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}

}
