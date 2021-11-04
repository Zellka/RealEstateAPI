package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Country
import com.university.jpapostgresql.repository.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class CountryController {

    @Autowired
    lateinit var repository: CountryRepository

    @PostMapping("/countries")
    fun addCountry(@RequestBody country: Country, uri: UriComponentsBuilder): ResponseEntity<Country> {
        val persistedCountry = repository.save(country)
        if (ObjectUtils.isEmpty(persistedCountry)) {
            return ResponseEntity<Country>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/country/{id}").buildAndExpand(country.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @DeleteMapping("/countries/{id}")
    fun removeCountryById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val country = repository.findById(id)
        if (country.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/countries")
    fun removeCountries(): ResponseEntity<Void> {
        val countries = repository.findAll()
        if (countries.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}