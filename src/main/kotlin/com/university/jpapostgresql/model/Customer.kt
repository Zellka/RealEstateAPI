package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class Customer(
    val organization: String,
    val phone: String,
    val city: String,
    val address: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country,
    @OneToMany(mappedBy = "customerId")
    val order: List<OrderRealty>,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)