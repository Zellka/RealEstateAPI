package com.university.jpapostgresql.model

import java.time.LocalDate
import javax.persistence.*

@Entity
data class OrderRealty(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    @ManyToOne
    @JoinColumn(name = "realty_id")
    val realtyId: Realty,
    @ManyToOne
    @JoinColumn(name = "employee_id")
    val employeeId: Employee,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customerId: Customer,
    val price: Long,
    val date: LocalDate
)