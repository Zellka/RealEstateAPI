package com.university.jpapostgresql.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Category(
    val category: String,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)