package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Realty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RealtyRepository : JpaRepository<Realty, Long> {
    @Query("select r from Realty r where r.ownerId.organization = :organization")
    fun findByOwnerOrganization(organization: String): List<Realty>

    @Query("select avg(r.price) from Realty r where r.categoryId.id = :categoryId")
    fun findAvgPrice(categoryId: Long): Long

    @Query("select avg(r.price) from Realty r where r.categoryId.id = :categoryId and r.city = :city")
    fun findAvgPrice(categoryId: Long, city: String): Long

    @Query("select r from Realty r, OrderRealty o where r.id = o.realtyId.id")
    fun findSoldRealty(): List<Realty>

    @Query("select r from Realty r, OrderRealty o where r.id = o.realtyId.id and o.date > :date")
    fun findSoldRealtyByDate(date: LocalDate): List<Realty>

    @Query("select r from Realty r where (current_date - r.date) > 180")
    fun findOldRealtyByDate(): List<Realty>

    @Query("select count(r) from Realty r, OrderRealty o where r.id = o.realtyId.id and r.categoryId.id = :categoryId")
    fun findCountSoldRealtyByCategoryId(categoryId: Long): Long
}