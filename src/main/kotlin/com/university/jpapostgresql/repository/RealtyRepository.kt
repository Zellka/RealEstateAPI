package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Realty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RealtyRepository : JpaRepository<Realty, Long> {
    @Query("select r from Realty r where r.ownerId.organization = :organization")
    fun findByOwnerOrganization(organization: String): List<Realty>
}