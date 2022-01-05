package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TypeRepository : JpaRepository<Type, Long> {
}
