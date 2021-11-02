package com.university.jpapostgresql.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Employee(
    val surname: String,
    val name: String,
    val address: String,
    val phone: String,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
)


//@Entity
//class Customer(
//		val firstName: String,
//		val lastName: String,
//		@Id @GeneratedValue(strategy = GenerationType.AUTO)
//		val id: Long = -1) {
//
//	private constructor() : this("", "")
//}

//@Entity
//@NoArgsConstructor
//@Table(name = "GADGET")
//data class Gadget(
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    val gadgetId: Long,
//    val gadgetName: String,
//    val gadgetCategory: String?,
//    val gagdetAvailability: Boolean = true,
//    val gadgetPrice: Double
//)