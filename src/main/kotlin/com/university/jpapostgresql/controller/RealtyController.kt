package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Realty
import com.university.jpapostgresql.repository.RealtyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class RealtyController {

    @Autowired
    lateinit var repository: RealtyRepository

    @RequestMapping("/realty-list")
    fun findAll(): ResponseEntity<List<Realty>> {
        val realtyList = repository.findAll()
        if (realtyList.isEmpty()) {
            return ResponseEntity<List<Realty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Realty>>(realtyList, HttpStatus.OK)
    }

    @RequestMapping("/realty-list/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Realty> {
        val realty = repository.findById(id)
        if (realty.isPresent) {
            return ResponseEntity<Realty>(realty.get(), HttpStatus.OK)
        }
        return ResponseEntity<Realty>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/realty-list")
    fun addRealty(@RequestBody realty: Realty, uri: UriComponentsBuilder): ResponseEntity<Realty> {
        val persistedRealty = repository.save(realty)
        if (ObjectUtils.isEmpty(persistedRealty)) {
            return ResponseEntity<Realty>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/realty/{id}").buildAndExpand(realty.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/realty-list/{id}")
    fun updateRealtyById(@PathVariable("id") id: Long, @RequestBody realty: Realty): ResponseEntity<Realty> {
        return repository.findById(id).map { realtyDetails ->
            val updatedRealty: Realty = realtyDetails.copy(
                name = realty.name,
                address = realty.address,
                description = realty.description,
                price = realty.price,
                categoryId = realty.categoryId,
                ownerId = realty.ownerId
            )
            ResponseEntity(repository.save(updatedRealty), HttpStatus.OK)
        }.orElse(ResponseEntity<Realty>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/realty-list/{id}")
    fun removeRealtyById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val realty = repository.findById(id)
        if (realty.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/realty-list")
    fun removeRealtyList(): ResponseEntity<Void> {
        val realtyList = repository.findAll()
        if (realtyList.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}