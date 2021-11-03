package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.OwnerRealty
import com.university.jpapostgresql.repository.OwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class OwnerController {

    @Autowired
    lateinit var repository: OwnerRepository

    @RequestMapping("/owners")
    fun findAll(): ResponseEntity<List<OwnerRealty>> {
        val owners = repository.findAll().toList()
        if (owners.isEmpty()) {
            return ResponseEntity<List<OwnerRealty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<OwnerRealty>>(owners, HttpStatus.OK)
    }

    @RequestMapping("/owners/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<OwnerRealty> {
        val owner = repository.findById(id)
        if (owner.isPresent) {
            return ResponseEntity<OwnerRealty>(owner.get(), HttpStatus.OK)
        }
        return ResponseEntity<OwnerRealty>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/owners")
    fun addOwner(@RequestBody owner: OwnerRealty, uri: UriComponentsBuilder): ResponseEntity<OwnerRealty> {
        val persistedOwner = repository.save(owner)
        if (ObjectUtils.isEmpty(persistedOwner)) {
            return ResponseEntity<OwnerRealty>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/owner/{id}").buildAndExpand(owner.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/owners/{id}")
    fun updateOwnerById(@PathVariable("id") id: Long, @RequestBody owner: OwnerRealty): ResponseEntity<OwnerRealty> {
        return repository.findById(id).map { ownerDetails ->
            val updatedOwner: OwnerRealty = ownerDetails.copy(
                organization = owner.organization,
                phone = owner.phone,
                city = owner.city,
                address = owner.address
            )
            ResponseEntity(repository.save(updatedOwner), HttpStatus.OK)
        }.orElse(ResponseEntity<OwnerRealty>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/owners/{id}")
    fun removeOwnerById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val owner = repository.findById(id)
        if (owner.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/owners")
    fun removeOwners(): ResponseEntity<Void> {
        val owners = repository.findAll()
        if (owners.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}