package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class Employee(
    val surname: String,
    val name: String,
    val address: String,
    val phone: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)