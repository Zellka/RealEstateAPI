package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.OrderRealty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderRealty, Long> {
}