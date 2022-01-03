package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    val name: String,
    val phone: String,
    val address: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country,
    val numYearWork: Long
)