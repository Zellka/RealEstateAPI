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
    @ManyToOne
    @JoinColumn(name = "type_id")
    val typeId: Type,
    @ManyToOne
    @JoinColumn(name = "owner_id")
    val ownerId: OwnerRealty,
    val date: LocalDate,
    val price: Long,
    val address: String,
    @ManyToOne
    @JoinColumn(name = "country_id")
    val countryId: Country
)