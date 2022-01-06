package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.Category
import com.university.jpapostgresql.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class CategoryController {

    @Autowired
    lateinit var repository: CategoryRepository

    @RequestMapping("/categories")
    fun findAll(): ResponseEntity<List<Category>> {
        val categories = repository.findAll()
        if (categories.isEmpty()) {
            return ResponseEntity<List<Category>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<Category>>(categories, HttpStatus.OK)
    }

    @RequestMapping("/categories/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Category> {
        val category = repository.findById(id)
        if (category.isPresent) {
            return ResponseEntity<Category>(category.get(), HttpStatus.OK)
        }
        return ResponseEntity<Category>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/categories")
    fun addCategory(@RequestBody category: Category, uri: UriComponentsBuilder): ResponseEntity<Category> {
        val persistedCategory = repository.save(category)
        if (ObjectUtils.isEmpty(persistedCategory)) {
            return ResponseEntity<Category>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/category/{id}").buildAndExpand(category.id).toUri());
        return ResponseEntity(persistedCategory, HttpStatus.CREATED)
    }

    @PutMapping("/categories/{id}")
    fun updateCategoryById(@PathVariable("id") id: Long, @RequestBody category: Category): ResponseEntity<Category> {
        return repository.findById(id).map { categoryDetails ->
            val updatedCategory: Category = categoryDetails.copy(
                category = category.category
            )
            ResponseEntity(repository.save(updatedCategory), HttpStatus.OK)
        }.orElse(ResponseEntity<Category>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/categories/{id}")
    fun removeCategoryById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val category = repository.findById(id)
        if (category.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/categories")
    fun removeCategories(): ResponseEntity<Void> {
        val categories = repository.findAll()
        if (categories.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}
