package com.university.jpapostgresql.model

import java.time.LocalDate
import javax.persistence.*

@Entity
data class Realty(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
    val description: String,
    @ManyToOne
    @JoinColumn(name = "category_id")
    val categoryId: Category,
    val square: Long,
    val numFloor: Int,
    @ManyToOne
    @JoinColumn(name = "owner_id")
    val ownerId: OwnerRealty,
    val date: LocalDate,
    val numViews: Int,
    val price: Long,
    val address: String,
    val city: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country
)