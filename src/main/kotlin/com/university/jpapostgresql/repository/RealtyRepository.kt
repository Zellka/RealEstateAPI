package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Realty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RealtyRepository : JpaRepository<Realty, Long> {
}