package com.university.jpapostgresql.model

import javax.persistence.*

@Entity
data class Realty(
    val name: String,
    val address: String,
    val description: String,
    val price: Long,
    @ManyToOne
    @JoinColumn(name = "category_id")
    val categoryId: Category,
    @ManyToOne
    @JoinColumn(name = "owner_id")
    val ownerId: OwnerRealty,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)