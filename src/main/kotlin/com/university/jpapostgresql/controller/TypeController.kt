package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Type
import com.university.jpapostgresql.repository.TypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class TypeController {

    @Autowired
    lateinit var repository: TypeRepository

    @RequestMapping("/types")
    fun findAll(): ResponseEntity<List<Type>> {
        val types = repository.findAll()
        if (types.isEmpty()) {
            return ResponseEntity<List<Type>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Type>>(types, HttpStatus.OK)
    }

    @RequestMapping("/types/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Type> {
        val type = repository.findById(id)
        if (type.isPresent) {
            return ResponseEntity<Type>(type.get(), HttpStatus.OK)
        }
        return ResponseEntity<Type>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/types")
    fun addType(@RequestBody type: Type, uri: UriComponentsBuilder): ResponseEntity<Type> {
        val persistedType = repository.save(type)
        if (ObjectUtils.isEmpty(persistedType)) {
            return ResponseEntity<Type>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/type/{id}").buildAndExpand(type.id).toUri());
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/types/{id}")
    fun updateTypeById(@PathVariable("id") id: Long, @RequestBody type: Type): ResponseEntity<Type> {
        return repository.findById(id).map { categoryDetails ->
            val updatedType: Type = categoryDetails.copy(
                type = type.type
            )
            ResponseEntity(repository.save(updatedType), HttpStatus.OK)
        }.orElse(ResponseEntity<Type>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/types/{id}")
    fun removeTypeById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val type = repository.findById(id)
        if (type.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/types")
    fun removeTypes(): ResponseEntity<Void> {
        val types = repository.findAll()
        if (types.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}
