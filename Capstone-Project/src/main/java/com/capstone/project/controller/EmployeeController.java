package com.capstone.project.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capstone.project.bean.Employee;
import com.capstone.project.exceptions.ResourceNotFoundException;
import com.capstone.project.service.EmployeeService;
import com.capstone.project.utils.HeaderUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeService empService;

	@GetMapping("/getEmployee")
	public @ResponseBody List<Employee> getAllEmployee() {
		return empService.getAllEmployee();
	}

	@GetMapping("/getEmployee/{empId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer empId)
			throws ResourceNotFoundException {
		Employee employee = empService.findEmployeeById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/addEmployee")
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		empService.save(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getEmpId())
                .toUri();
		return ResponseEntity.created(location).build();
	}

	//	@PostMapping("/updateEmployee")
	//	public void updateEmployee(@RequestBody List<Employee> employeeList) {
	//		empService.updateEmployee(employeeList);
	//	}
	
	@PutMapping("/updateEmployee")
	public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) throws ResourceNotFoundException {
        Integer id = employee.getEmpId();
        empService.findEmployeeById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        Employee result = empService.save(employee);
        return ResponseEntity.ok(result);
    }


//	@ApiOperation(value = "Update an employee")
//	@PutMapping("/updateEmployee/{Id}")
//	public ResponseEntity<Employee> updateEmployee(@PathVariable Integer Id,
//			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
//		Employee employee = empService.findEmployeeById(Id)
//				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + Id));
//		employee.setEmailId(employeeDetails.getEmailId());
//		employee.setEmpName(employeeDetails.getEmpName());
//		employee.setDepartmentName(employeeDetails.getDepartmentName());
//		final Employee result = empService.save(employee);
//		return ResponseEntity.ok(result);
//	}


	@ApiOperation(value = "Delete an employee")
	@DeleteMapping("/deleteEmployee/{Id}")
	public ResponseEntity<Void> deleteEmployee(
			@PathVariable Integer Id)
			throws ResourceNotFoundException {
		Employee employee = empService.findEmployeeById(Id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + Id));
		empService.deleteEmployee(employee);
//		Map<String, Boolean> response = new HashMap<>();
//		response.put("deleted", Boolean.TRUE);
//		return response;
		return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("employee", Id.toString())).build();
	}
}
