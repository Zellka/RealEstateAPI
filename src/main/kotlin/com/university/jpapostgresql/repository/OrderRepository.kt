package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.OrderRealty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderRealty, Long> {
    @Query("select r from OrderRealty r where r.employeeId.id = :id")
    fun findByEmployeeId(id: Long): List<OrderRealty>
}