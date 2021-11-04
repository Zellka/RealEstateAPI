package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Customer
import com.university.jpapostgresql.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

//http://localhost:8080/api/customers

@RestController
@RequestMapping("/api")
class CustomerController {

    @Autowired
    lateinit var repository: CustomerRepository

    @RequestMapping("/customers")
    fun findAll(): ResponseEntity<List<Customer>> {
        val customers = repository.findAll()
        if (customers.isEmpty()) {
            return ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Customer>>(customers, HttpStatus.OK)
    }

    @RequestMapping("/customers/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Customer> {
        val customer = repository.findById(id)
        if (customer.isPresent) {
            return ResponseEntity<Customer>(customer.get(), HttpStatus.OK)
        }
        return ResponseEntity<Customer>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/customers")
    fun addCustomer(@RequestBody customer: Customer, uri: UriComponentsBuilder): ResponseEntity<Customer> {
        val persistedCustomer = repository.save(customer)
        if (ObjectUtils.isEmpty(persistedCustomer)) {
            return ResponseEntity<Customer>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/customer/{id}").buildAndExpand(customer.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/customers/{id}")
    fun updateCustomerById(@PathVariable("id") id: Long, @RequestBody customer: Customer): ResponseEntity<Customer> {
        return repository.findById(id).map { customerDetails ->
            val updatedCustomer: Customer = customerDetails.copy(
                organization = customer.organization,
                phone = customer.phone,
                city = customer.city,
                address = customer.address,
                countryId = customer.countryId
            )
            ResponseEntity(repository.save(updatedCustomer), HttpStatus.OK)
        }.orElse(ResponseEntity<Customer>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/customers/{id}")
    fun removeCustomerById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val customer = repository.findById(id)
        if (customer.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/customers")
    fun removeCustomers(): ResponseEntity<Void> {
        val customers = repository.findAll()
        if (customers.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}