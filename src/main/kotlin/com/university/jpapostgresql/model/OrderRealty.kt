package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class OrderRealty(
    @OneToMany(mappedBy = "id")
    val realtyId: List<Realty>,
    @ManyToOne
    @JoinColumn(name = "employee_id")
    val employeeId: Employee,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customerId: Customer,
    val price: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)