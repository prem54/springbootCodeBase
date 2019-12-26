package com.capstone.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.capstone.project.bean.Employee;
import com.capstone.project.config.Constants;
import com.capstone.project.repository.EmployeeRepository;


@ActiveProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
@SpringBootTest
@DisplayName("Test Service")
class EmployeeServiceTest {

	@Autowired
	EmployeeService service;

	@MockBean
	private EmployeeRepository repository;


	//	EmployeeRepository repository = Mockito.mock(EmployeeRepository.class);

	//	@Test
	//	public void contextLoads() {
	//	}

	//	@Test
	//	@DisplayName("Test get all users")
	//	public void test_getAllUsers() {
	//		when(repository.findAll()).thenReturn(Stream
	//				.of(new Employee(101, "Test-101", "Test-101@it.com", "IT"), new Employee(102, "Test-102", "Test-102@it.com", "IT")).collect(Collectors.toList()));
	//		assertEquals(2, service.getAllEmployee().size());
	//	}

	//or

	@Test
	@DisplayName("ServiceTest-All Employee")
	public void find_AllEmployee_Ok() {
		// given
		Employee employee1 = new Employee(1, "Test-101", "Test-101@it.com", "IT");
		Employee employee2 = new Employee(2, "Test-102", "Test-102@it.com", "IT");
		when(repository.findAll()).thenReturn(Stream.of(employee1, employee2).collect(Collectors.toList()));
		// when
		List<Employee> result = service.getAllEmployee();
		// then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getEmpName()).isEqualTo(employee1.getEmpName());
		assertThat(result.get(0).getEmpId()).isEqualTo(employee1.getEmpId());
		assertThat(result.get(0).getDepartmentName()).isEqualTo(employee1.getDepartmentName());
		assertThat(result.get(0).getEmailId()).isEqualTo(employee1.getEmailId());
		assertThat(result.get(1).getEmpName()).isEqualTo(employee2.getEmpName());
		assertThat(result.get(1).getEmpId()).isEqualTo(employee2.getEmpId());
		assertThat(result.get(1).getDepartmentName()).isEqualTo(employee2.getDepartmentName());
		assertThat(result.get(1).getEmailId()).isEqualTo(employee2.getEmailId());
	}


	@Test
	@DisplayName("ServiceTest-Find Employee by Id") 
	public void find_EmployeeById_Ok() {
		Optional<Employee> employee = Optional.of(new Employee(1,"Test-101", "Test-101@it.com", "IT"));
		when(repository.findById(1)).thenReturn(employee);
		assertEquals(employee, service.findEmployeeById(1));
	}


	@Test
	@DisplayName("ServiceTest-Save Employee")
	public void Save_Employee_Ok() {
		Employee employee = new Employee(1, "Test-101", "Test-101@it.com", "IT");
		when(repository.save(employee)).thenReturn(employee);
		assertEquals(employee, service.save(employee));
	}

	@Test
	@DisplayName("ServiceTest-Delete Employee")
	public void delete_Employee_Ok() {
		Employee employee = new Employee(1, "Test-101", "Test-101@it.com", "IT");
		service.deleteEmployee(employee);
		verify(repository, times(1)).delete(employee);
	}

	@Test
	@DisplayName("ServiceTest-Update Employee")
	public void update_Employee_Ok() {
		Employee updatedEmployee = new Employee(1, "Test-101", "Test-101@it.com", "IT");
		when(repository.save(any(Employee.class))).thenReturn(updatedEmployee);

		assertEquals(updatedEmployee, service.save(updatedEmployee));

		//		verify(repository, times(1)).findById(1);
		//		verify(repository, times(1)).save(any(Employee.class));
	}

}
