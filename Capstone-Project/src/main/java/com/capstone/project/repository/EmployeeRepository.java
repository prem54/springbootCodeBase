package com.capstone.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.project.bean.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}
