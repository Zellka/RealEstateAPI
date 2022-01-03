package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    @Query("select e from Employee e where e.numYearWork > :numYearWork")
    fun findByNumYearWork(numYearWork: Long): List<Employee>
}