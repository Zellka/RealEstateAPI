package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {
}