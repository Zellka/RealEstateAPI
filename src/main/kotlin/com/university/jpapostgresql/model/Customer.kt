package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    val organization: String,
    val phone: String,
    val address: String,
    val city: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country
)