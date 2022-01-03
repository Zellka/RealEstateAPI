package com.university.jpapostgresql.model

import java.time.LocalDate
import javax.persistence.*

@Entity
data class Contract(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    @OneToOne
    @JoinColumn(name = "order_realty_id")
    val orderRealtyId: OrderRealty,
    @ManyToOne
    @JoinColumn(name = "employee_id")
    val employeeId: Employee,
    val price: Long,
    val date: LocalDate
)