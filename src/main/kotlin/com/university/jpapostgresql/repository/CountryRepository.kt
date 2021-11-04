package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, Long> {
}