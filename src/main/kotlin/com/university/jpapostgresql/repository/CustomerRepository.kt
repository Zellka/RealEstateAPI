package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.phone = :phone")
    fun findByPhone(phone: String): Optional<Customer>
}
