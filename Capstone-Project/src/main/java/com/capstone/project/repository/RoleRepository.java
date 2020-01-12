package com.capstone.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.project.bean.Role;
import com.capstone.project.bean.enumeration.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByName(RoleName roleName);

}
