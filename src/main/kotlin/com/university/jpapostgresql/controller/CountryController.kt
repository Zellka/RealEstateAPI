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

    @RequestMapping("/countries")
    fun findAll(): ResponseEntity<List<Country>> {
        val countries = repository.findAll()
        if (countries.isEmpty()) {
            return ResponseEntity<List<Country>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Country>>(countries, HttpStatus.OK)
    }

    @RequestMapping("/countries/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Country> {
        val country = repository.findById(id)
        if (country.isPresent) {
            return ResponseEntity<Country>(country.get(), HttpStatus.OK)
        }
        return ResponseEntity<Country>(HttpStatus.NOT_FOUND)
    }

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

    @PutMapping("/countries/{id}")
    fun updateCountryById(@PathVariable("id") id: Long, @RequestBody country: Country): ResponseEntity<Country> {
        return repository.findById(id).map { countryDetails ->
            val updatedCountry: Country = countryDetails.copy(
                country = country.country
            )
            ResponseEntity(repository.save(updatedCountry), HttpStatus.OK)
        }.orElse(ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR))
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
