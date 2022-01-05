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
import java.time.LocalDate

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

    @RequestMapping("/realty-list/owner/{organization}")
    fun findByOwnerOrganization(@PathVariable organization: String): ResponseEntity<List<Realty>> {
        val realtyList = repository.findByOwnerOrganization(organization)
        if (realtyList.isEmpty()) {
            return ResponseEntity<List<Realty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Realty>>(realtyList, HttpStatus.OK)
    }

    @RequestMapping("/realty-list/old-realty")
    fun findOldRealtyByDate(): ResponseEntity<List<Realty>> {
        val realtyList = repository.findOldRealtyByDate()
        if (realtyList.isEmpty()) {
            return ResponseEntity<List<Realty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Realty>>(realtyList, HttpStatus.OK)
    }

    @RequestMapping("/realty-list/avg-price/{categoryId}")
    fun findAvgPrice(@PathVariable categoryId: Long) =
        repository.findAvgPrice(categoryId)

    @RequestMapping("/realty-list/count-sold-realty/{categoryId}")
    fun findCountSoldRealtyByCategoryId(@PathVariable categoryId: Long) =
        repository.findCountSoldRealtyByCategoryId(categoryId)

    @RequestMapping("/realty-list/sold-realty")
    fun findSoldRealty(): ResponseEntity<List<Realty>> {
        val realtyList = repository.findSoldRealty()
        if (realtyList.isEmpty()) {
            return ResponseEntity<List<Realty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Realty>>(realtyList, HttpStatus.OK)
    }

    @RequestMapping("/realty-list/sold-realty/date/{year}/{month}/{day}")
    fun findSoldRealtyByDate(@PathVariable year: Int, @PathVariable month: Int, @PathVariable day: Int): ResponseEntity<List<Realty>> {
        val realtyList = repository.findSoldRealtyByDate(LocalDate.of(year, month, day))
        if (realtyList.isEmpty()) {
            return ResponseEntity<List<Realty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Realty>>(realtyList, HttpStatus.OK)
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
                description = realty.description,
                categoryId = realty.categoryId,
                typeId = realty.typeId,
                ownerId = realty.ownerId,
                date = realty.date,
                price = realty.price,
                address = realty.address,
                countryId = realty.countryId
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
