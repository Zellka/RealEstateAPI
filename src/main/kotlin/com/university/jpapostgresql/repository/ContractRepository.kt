package com.university.jpapostgresql.repository

import com.university.jpapostgresql.model.Contract
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContractRepository : JpaRepository<Contract, Long> {
}