package com.capstone.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.project.bean.Employee;
import com.capstone.project.repository.EmployeeRepository;


@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository empRepo;
	
	public Employee save(Employee employee) {
		return empRepo.save(employee);
	}
	
	Function<Employee, Employee> save = employee -> empRepo.save(employee);
	
	public List<Employee> getAllEmployee(){
		List<Employee> employeeList = new ArrayList<Employee>();
		empRepo.findAll().forEach(emp->employeeList.add(emp));
		return employeeList;
	}
	public Optional<Employee> findEmployeeById(Integer Id) {
		return empRepo.findById(Id);
	}
	
	public void updateEmployee(List<Employee> employeeList) {
		empRepo.saveAll(employeeList);
	}
	public void deleteEmployee(Employee employee) {
		empRepo.delete(employee);
	}
	
}
