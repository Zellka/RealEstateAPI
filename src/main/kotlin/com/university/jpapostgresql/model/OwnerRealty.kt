package com.university.jpapostgresql.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class OwnerRealty (
    val organization: String,
    val phone: String,
    val city: String,
    val address: String,
    //val country: Country
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)