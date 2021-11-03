package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.OwnerRealty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<OwnerRealty, Long> {
}