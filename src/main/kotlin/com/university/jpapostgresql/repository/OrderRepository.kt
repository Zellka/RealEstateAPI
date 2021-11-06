package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.OrderRealty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface OrderRepository : JpaRepository<OrderRealty, Long> {
    @Query("select o from OrderRealty o where o.employeeId.id = :id")
    fun findByEmployeeId(id: Long): List<OrderRealty>

    @Query("select o from OrderRealty o where o.date > :date")
    fun findByCreatedDateAfter(date: LocalDate): List<OrderRealty>
}