package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Contract
import com.university.jpapostgresql.repository.ContractRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class ContractController {

    @Autowired
    lateinit var repository: ContractRepository

    @RequestMapping("/contracts")
    fun findAll(): ResponseEntity<List<Contract>> {
        val contracts = repository.findAll()
        if (contracts.isEmpty()) {
            return ResponseEntity<List<Contract>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Contract>>(contracts, HttpStatus.OK)
    }

    @RequestMapping("/contracts/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Contract> {
        val contract = repository.findById(id)
        if (contract.isPresent) {
            return ResponseEntity<Contract>(contract.get(), HttpStatus.OK)
        }
        return ResponseEntity<Contract>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/contracts")
    fun addContract(@RequestBody contract: Contract, uri: UriComponentsBuilder): ResponseEntity<Contract> {
        val persistedContract = repository.save(contract)
        if (ObjectUtils.isEmpty(persistedContract)) {
            return ResponseEntity<Contract>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/contract/{id}").buildAndExpand(contract.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/contract/{id}")
    fun updateContractById(@PathVariable("id") id: Long, @RequestBody contract: Contract): ResponseEntity<Contract> {
        return repository.findById(id).map { contractDetails ->
            val updatedContract: Contract = contractDetails.copy(
                orderRealtyId = contract.orderRealtyId,
                employeeId = contract.employeeId,
                price = contract.price,
                date = contract.date
            )
            ResponseEntity(repository.save(updatedContract), HttpStatus.OK)
        }.orElse(ResponseEntity<Contract>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/contracts/{id}")
    fun removeContractById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val contract = repository.findById(id)
        if (contract.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/contracts")
    fun removeContracts(): ResponseEntity<Void> {
        val contracts = repository.findAll()
        if (contracts.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}