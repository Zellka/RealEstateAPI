package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.OwnerRealty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<OwnerRealty, Long> {
    @Query("select o from OwnerRealty o, Realty r where o.id = r.ownerId.id and r.categoryId.id = :categoryId")
    fun findByCategoryRealty(categoryId: Long): List<OwnerRealty>

    @Query("select o from OwnerRealty o, Realty r where o.id = r.ownerId.id and r.categoryId.id = :categoryId and r.countryId.id = :countryId")
    fun findByCategoryAndCountry(categoryId: Long, countryId: Long): List<OwnerRealty>
}