package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Employee
import com.university.jpapostgresql.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class EmployeeController {

    @Autowired
    lateinit var repository: EmployeeRepository

    @RequestMapping("/employees")
    fun findAll(): ResponseEntity<List<Employee>> {
        val employees = repository.findAll()
        if (employees.isEmpty()) {
            return ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Employee>>(employees, HttpStatus.OK)
    }

    @RequestMapping("/employees/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Employee> {
        val employee = repository.findById(id)
        if (employee.isPresent) {
            return ResponseEntity<Employee>(employee.get(), HttpStatus.OK)
        }
        return ResponseEntity<Employee>(HttpStatus.NOT_FOUND)
    }

    @RequestMapping("/employees/num-year-worked/{num}")
    fun findByNumYearWork(@PathVariable num: Long): ResponseEntity<List<Employee>> {
        val employees = repository.findByNumYearWork(num)
        if (employees.isEmpty()) {
            return ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Employee>>(employees, HttpStatus.OK)
    }

    @PostMapping("/employees")
    fun addEmployee(@RequestBody employee: Employee, uri: UriComponentsBuilder): ResponseEntity<Employee> {
        val persistedEmployee = repository.save(employee)
        if (ObjectUtils.isEmpty(persistedEmployee)) {
            return ResponseEntity<Employee>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/employee/{id}").buildAndExpand(employee.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/employees/{id}")
    fun updateEmployeeById(@PathVariable("id") id: Long, @RequestBody employee: Employee): ResponseEntity<Employee> {
        return repository.findById(id).map { employeeDetails ->
            val updatedEmployee: Employee = employeeDetails.copy(
                name = employee.name,
                phone = employee.phone,
                address = employee.address,
                countryId = employee.countryId,
                numYearWork = employee.numYearWork
            )
            ResponseEntity(repository.save(updatedEmployee), HttpStatus.OK)
        }.orElse(ResponseEntity<Employee>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/employees/{id}")
    fun removeEmployeeById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val employee = repository.findById(id)
        if (employee.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/employees")
    fun removeEmployees(): ResponseEntity<Void> {
        val employees = repository.findAll()
        if (employees.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}