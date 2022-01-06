package com.university.jpapostgresql.controller

import com.university.jpapostgresql.model.OrderRealty
import com.university.jpapostgresql.repository.OrderRepository
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
class OrderController {

    @Autowired
    lateinit var repository: OrderRepository

    @RequestMapping("/orders")
    fun findAll(): ResponseEntity<List<OrderRealty>> {
        val orders = repository.findAll()
        if (orders.isEmpty()) {
            return ResponseEntity<List<OrderRealty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<OrderRealty>>(orders, HttpStatus.OK)
    }

    @RequestMapping("/orders/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<OrderRealty> {
        val order = repository.findById(id)
        if (order.isPresent) {
            return ResponseEntity<OrderRealty>(order.get(), HttpStatus.OK)
        }
        return ResponseEntity<OrderRealty>(HttpStatus.NOT_FOUND)
    }

    @RequestMapping("/orders/date/{year}/{month}/{day}")
    fun findByCreatedDateAfter(
        @PathVariable year: Int,
        @PathVariable month: Int,
        @PathVariable day: Int
    ): ResponseEntity<List<OrderRealty>> {
        val orders = repository.findByCreatedDateAfter(LocalDate.of(year, month, day))
        if (orders.isEmpty()) {
            return ResponseEntity<List<OrderRealty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<OrderRealty>>(orders, HttpStatus.OK)
    }

    @RequestMapping("/orders/customer/{id}")
    fun findByCustomer(
        @PathVariable id: Long
    ): ResponseEntity<List<OrderRealty>> {
        val orders = repository.findByCustomer(id)
        if (orders.isEmpty()) {
            return ResponseEntity<List<OrderRealty>>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<List<OrderRealty>>(orders, HttpStatus.OK)
    }

    @PostMapping("/orders")
    fun addOrder(@RequestBody order: OrderRealty, uri: UriComponentsBuilder): ResponseEntity<OrderRealty> {
        val persistedOrder = repository.save(order)
        if (ObjectUtils.isEmpty(persistedOrder)) {
            return ResponseEntity<OrderRealty>(HttpStatus.BAD_REQUEST)
        }
        val headers = HttpHeaders()
        headers.setLocation(uri.path("/order/{id}").buildAndExpand(order.id).toUri());
        return ResponseEntity(persistedOrder, HttpStatus.CREATED)
    }

    @PutMapping("/order/{id}")
    fun updateOrderById(@PathVariable("id") id: Long, @RequestBody order: OrderRealty): ResponseEntity<OrderRealty> {
        return repository.findById(id).map { orderDetails ->
            val updatedOrder: OrderRealty = orderDetails.copy(
                realtyId = order.realtyId,
                customerId = order.customerId,
                price = order.price,
                date = order.date
            )
            ResponseEntity(repository.save(updatedOrder), HttpStatus.OK)
        }.orElse(ResponseEntity<OrderRealty>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/orders/{id}")
    fun removeOrderById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val order = repository.findById(id)
        if (order.isPresent) {
            repository.deleteById(id)
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/orders")
    fun removeOrders(): ResponseEntity<Void> {
        val orders = repository.findAll()
        if (orders.isEmpty()) {
            return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
        repository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}
